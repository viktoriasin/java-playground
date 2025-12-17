package ru.sinvic.patterns.cost.calculation.system;

public record DeliveryParameters(double weight, double distance) {
    public DeliveryParameters {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than zero!");
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance must be greater than zero!");
        }
    }
}
