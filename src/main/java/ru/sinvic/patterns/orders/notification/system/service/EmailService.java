package ru.sinvic.patterns.orders.notification.system.service;

public interface EmailService {
    void sendEmail(String email, String text);
}
