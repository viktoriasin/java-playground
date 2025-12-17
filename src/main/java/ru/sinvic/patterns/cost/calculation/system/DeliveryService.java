package ru.sinvic.patterns.cost.calculation.system;

import java.math.BigDecimal;


public class DeliveryService {
    public static BigDecimal calculateDelivery(
        DeliveryStrategy deliveryStrategy, DeliveryParameters deliveryParameters) {
        checkParameters(deliveryStrategy, deliveryParameters);
        return deliveryStrategy.calculateDelivery(deliveryParameters);
    }

    private static void checkParameters(DeliveryStrategy deliveryStrategy, DeliveryParameters deliveryParameters) {
        if (deliveryStrategy == null) {
            throw new IllegalArgumentException("Delivery strategy does not found!");
        }
        if (deliveryParameters == null) {
            throw new IllegalArgumentException("Delivery parameters does not found!");
        }
    }
}
