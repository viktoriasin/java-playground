package ru.sinvic.patterns.orders.notification.system;

public interface OrderEventListener {
    void update(Order order);
}
