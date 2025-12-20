package ru.sinvic.patterns.orders.notification.system;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sinvic.patterns.orders.notification.system.exception.WarehouseServiceException;
import ru.sinvic.patterns.orders.notification.system.model.Order;
import ru.sinvic.patterns.orders.notification.system.service.WarehouseService;

@Slf4j
@RequiredArgsConstructor
public class WarehousesNotifier implements OrderEventListener {
    private final WarehouseService warehouseService;

    @Override
    public void update(@NonNull Order order) {
        try {
            warehouseService.sendMessage(order);
        } catch (WarehouseServiceException ex) {
            log.error(STR."Can not send message to warehouse. \{ex.getMessage()}");
        }
    }
}
