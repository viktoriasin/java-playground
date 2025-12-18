package ru.sinvic.patterns.orders.notification.system;

import lombok.NonNull;
import ru.sinvic.patterns.orders.notification.system.model.Order;

public interface OrderEventListener {
    void notify(@NonNull Order order);
}
