package ru.sinvic.patterns.cost.calculation.system;

import java.math.BigDecimal;

public class PostDeliveryStrategy implements DeliveryStrategy {
    public static BigDecimal RATE_PER_KM = new BigDecimal(5);
    @Override
    public BigDecimal calculateDelivery(DeliveryParameters deliveryParameters) {
        return RATE_PER_KM.multiply(BigDecimal.valueOf(deliveryParameters.distance()));
    }
}
