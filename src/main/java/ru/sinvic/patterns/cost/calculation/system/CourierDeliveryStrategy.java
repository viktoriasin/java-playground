package ru.sinvic.patterns.cost.calculation.system;

import java.math.BigDecimal;

public class CourierDeliveryStrategy implements DeliveryStrategy {
    public static final BigDecimal BASE_PRICE = new BigDecimal(100);
    public static final BigDecimal WEIGHT_SURCHARGE = new BigDecimal(5);

    @Override
    public BigDecimal calculateDelivery(DeliveryParameters deliveryParameters) {
        return BASE_PRICE.add(WEIGHT_SURCHARGE.multiply(BigDecimal.valueOf(deliveryParameters.weight())));
    }
}
