package ru.sinvic.patterns.orders.notification.system.exception;

public class MessengerServiceException extends RuntimeException {
    public MessengerServiceException(String message) {
        super(message);
    }
}
