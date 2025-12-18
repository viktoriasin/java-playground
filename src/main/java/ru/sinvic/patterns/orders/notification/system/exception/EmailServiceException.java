package ru.sinvic.patterns.orders.notification.system.exception;

public class EmailServiceException extends RuntimeException {
    public EmailServiceException(String message) {
        super(message);
    }
}
