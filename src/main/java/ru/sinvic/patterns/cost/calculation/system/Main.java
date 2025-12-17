package ru.sinvic.patterns.cost.calculation.system;

public class Main {
    public static void main(String[] args) {
        DeliveryStrategy deliveryStrategy1 = new CourierDeliveryStrategy();
        DeliveryStrategy deliveryStrategy2 = new PickupDeliveryStrategy();

        DeliveryParameters deliveryParameters = new DeliveryParameters(10, 10);
        System.out.println(DeliveryService.calculateDelivery(deliveryStrategy1, deliveryParameters));
        System.out.println(DeliveryService.calculateDelivery(deliveryStrategy2, deliveryParameters));


    }
}
