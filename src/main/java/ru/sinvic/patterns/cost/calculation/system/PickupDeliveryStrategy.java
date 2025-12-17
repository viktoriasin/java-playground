package ru.sinvic.patterns.cost.calculation.system;

import java.math.BigDecimal;

public class PickupDeliveryStrategy implements DeliveryStrategy {
    @Override
    public BigDecimal calculateDelivery(DeliveryParameters deliveryParameters) {
        return BigDecimal.ZERO;
    }
}
