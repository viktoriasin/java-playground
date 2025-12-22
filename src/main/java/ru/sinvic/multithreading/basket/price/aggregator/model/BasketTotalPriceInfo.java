package ru.sinvic.multithreading.basket.price.aggregator.model;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Optional;

public record BasketTotalPriceInfo(long basketId,
                                   @NonNull BigDecimal priceWIthDiscounts,
                                   Optional<BigDecimal> cashBack) {
    public BasketTotalPriceInfo {
        if (priceWIthDiscounts.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(STR."Total basket price can not be negative or zero! Get \{priceWIthDiscounts}");
        }
    }
}
