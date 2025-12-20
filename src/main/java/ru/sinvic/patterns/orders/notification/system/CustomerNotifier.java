package ru.sinvic.patterns.orders.notification.system;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sinvic.patterns.orders.notification.system.exception.EmailServiceException;
import ru.sinvic.patterns.orders.notification.system.exception.MessengerServiceException;
import ru.sinvic.patterns.orders.notification.system.model.Order;
import ru.sinvic.patterns.orders.notification.system.service.EmailService;
import ru.sinvic.patterns.orders.notification.system.service.MessengerService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CustomerNotifier implements OrderEventListener {

    private static final String MESSENGER_CHAT = "orders_updates"; // для простоты захардкодим
    private final EmailService emailService;
    private final MessengerService messengerService;

    private final List<String> clientsEmails; // для простоты зададим в конструкторе

    @Override
    public void notify(@NonNull Order order) {
        String text = STR."Order \{order.id()} has changed";
        sendEmail(text);
        sendMessage(text);
    }

    public void addClientEmail(@NonNull String email) {
        clientsEmails.add(email);
    }

    public void removeClientEmail(@NonNull String email) {
        clientsEmails.remove(email);
    }

    private void sendEmail(String emailText) {
        for (String clientEmail : clientsEmails) {
            try {
                emailService.sendEmail(clientEmail, emailText);
            } catch (EmailServiceException ex) {
                log.error(STR."Can not send email to \{clientEmail}. \{ex.getMessage()}");
            }
        }
    }

    private void sendMessage(String text) {
        try {
            messengerService.sendMessage(MESSENGER_CHAT, text);
        } catch (MessengerServiceException ex) {
            log.error(STR."Can not send message to \{MESSENGER_CHAT}. \{ex.getMessage()}");
        }
    }
}
