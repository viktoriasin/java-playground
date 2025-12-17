package ru.sinvic.patterns.cost.calculation.system;

import java.math.BigDecimal;

public interface DeliveryStrategy {
    BigDecimal calculateDelivery(DeliveryParameters deliveryParameters);
}
