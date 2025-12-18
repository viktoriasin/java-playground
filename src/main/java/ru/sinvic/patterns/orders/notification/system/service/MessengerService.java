package ru.sinvic.patterns.orders.notification.system.service;

public interface MessengerService {
    void sendMessage(String chatName, String text);
}
