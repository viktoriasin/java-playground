package ru.sinvic.patterns.cost.calculation.system;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class DeliveryStrategyTest {

    @Test
    public void shouldReturnCorrectPriceForCourierDelivery() {
        DeliveryStrategy deliveryStrategy = new CourierDeliveryStrategy();
        DeliveryParameters deliveryParameters = new DeliveryParameters(10, 5);
        assertTrue(BigDecimal.valueOf(150).compareTo(deliveryStrategy.calculateDelivery(deliveryParameters)) == 0);
    }

    @Test
    public void shouldReturnCorrectPriceForPickupDelivery() {
        DeliveryStrategy deliveryStrategy = new PickupDeliveryStrategy();
        DeliveryParameters deliveryParameters = new DeliveryParameters(10, 5);
        assertTrue(BigDecimal.ZERO.compareTo(deliveryStrategy.calculateDelivery(deliveryParameters)) == 0);
    }

    @Test
    public void shouldReturnCorrectPriceForPostDeliveryDelivery() {
        DeliveryStrategy deliveryStrategy = new PostDeliveryStrategy();
        DeliveryParameters deliveryParameters = new DeliveryParameters(10, 5);
        assertTrue(BigDecimal.valueOf(25).compareTo(deliveryStrategy.calculateDelivery(deliveryParameters)) == 0);
    }



}