package ru.sinvic.patterns.orders.notification.system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sinvic.patterns.orders.notification.system.model.Order;
import ru.sinvic.patterns.orders.notification.system.service.EmailService;
import ru.sinvic.patterns.orders.notification.system.service.MessengerService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerNotifierTest {
    @Captor
    private ArgumentCaptor<String> chatNameCaptor;
    @Captor
    private ArgumentCaptor<String> textCaptor;
    @Captor
    private ArgumentCaptor<String> emailCaptor;



    @Mock
    EmailService emailService;

    @Mock
    MessengerService messengerService;

    CustomerNotifier customerNotifier;

    List<String> emails = List.of("a@yandex.ru", "b@yandex.ru");
    Order order = new Order(1L, "Order", LocalDateTime.now());
    String messageText = STR."Order \{order.id()} has changed";
    String MESSENGER_CHAT = "orders_updates";

    @BeforeEach
    void setUp() {
        customerNotifier = new CustomerNotifier(emailService, messengerService, emails);
    }

    @Test
    void shouldSendEmailsAndMessagesAfterNotificationHappens() {
        customerNotifier.update(order);

        verify(messengerService, times(1)).sendMessage(ArgumentMatchers.any(), ArgumentMatchers.any());
        verify(emailService, times(2)).sendEmail(ArgumentMatchers.any(), ArgumentMatchers.any());


        verify(messengerService, times(1))
            .sendMessage(chatNameCaptor.capture(), textCaptor.capture());

        List<String> capturedChatNames = chatNameCaptor.getAllValues();
        List<String> capturedTexts = textCaptor.getAllValues();
        assertEquals(MESSENGER_CHAT, capturedChatNames.get(0));
        assertEquals(messageText, capturedTexts.get(0));

    }
}