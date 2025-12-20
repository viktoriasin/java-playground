package ru.sinvic.patterns.orders.notification.system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sinvic.patterns.orders.notification.system.model.Order;
import ru.sinvic.patterns.orders.notification.system.service.EmailService;
import ru.sinvic.patterns.orders.notification.system.service.MessengerService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerNotifierTest {
    @Mock
    private EmailService emailService;

    @Mock
    private MessengerService messengerService;

    @Captor
    private ArgumentCaptor<String> messengerChatCaptor;

    @Captor
    private ArgumentCaptor<String> messengerTextCaptor;

    @Captor
    private ArgumentCaptor<String> emailRecipientCaptor;

    @Captor
    private ArgumentCaptor<String> emailSubjectCaptor;

    private CustomerNotifier customerNotifier;

    private final List<String> emails = List.of("a@yandex.ru", "b@yandex.ru");
    private final Order order = new Order(1L, "Order", LocalDateTime.now());
    String text = STR."Order \{order.id()} has changed";

    @BeforeEach
    void setUp() {
        customerNotifier = new CustomerNotifier(emailService, messengerService, emails);
    }


    @Test
    void shouldSendSingleMessengerNotificationWithCorrectArgs() {
        customerNotifier.update(order);

        verify(messengerService).sendMessage(
            messengerChatCaptor.capture(),
            messengerTextCaptor.capture()
        );

        List<String> chatNames = messengerChatCaptor.getAllValues();
        List<String> texts = messengerTextCaptor.getAllValues();

        assertEquals(1, chatNames.size());
        String expectedChatName = "orders_updates";
        assertEquals(expectedChatName, chatNames.getFirst(), "Неверный чат для уведомления");
        assertEquals(text, texts.get(0), "Неверный текст сообщения");
    }

    @Test
    void shouldSendEmailsToAllSubscribedAddresses() {
        customerNotifier.update(order);

        verify(emailService, times(2)).sendEmail(
            emailRecipientCaptor.capture(),
            emailSubjectCaptor.capture()
        );

        List<String> recipients = emailRecipientCaptor.getAllValues();
        assertEquals(emails, recipients, "Письма должны быть отправлены на все указанные адреса");

        List<String> subjects = emailSubjectCaptor.getAllValues();
        assertTrue(subjects.stream().allMatch(s -> s.contains(text)));
    }

    @Test
    void shouldNotPerformOtherServiceCalls() {
        customerNotifier.update(order);

        verifyNoMoreInteractions(emailService, messengerService);
    }
}