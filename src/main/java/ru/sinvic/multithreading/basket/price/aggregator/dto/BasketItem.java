package ru.sinvic.multithreading.basket.price.aggregator.dto;

public record BasketItem(long basketId, long productId, int quantity) {
    public BasketItem {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Item quantity can not be negative or zero!");
        }
    }
}
