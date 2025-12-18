package ru.sinvic.patterns.orders.notification.system.service;

import lombok.NonNull;
import ru.sinvic.patterns.orders.notification.system.model.Order;

public interface WarehouseService {
    void notifyForOrder(@NonNull Order order);
}
