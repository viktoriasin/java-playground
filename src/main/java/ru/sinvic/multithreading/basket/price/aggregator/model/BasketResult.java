package ru.sinvic.multithreading.basket.price.aggregator.model;

import ru.sinvic.multithreading.basket.price.aggregator.exception.BasketException;

public record BasketResult(BasketTotalPriceInfo basketTotalPriceInfo, Exception ex) {
}
