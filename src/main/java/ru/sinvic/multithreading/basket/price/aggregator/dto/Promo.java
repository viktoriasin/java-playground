package ru.sinvic.multithreading.basket.price.aggregator.dto;

import java.math.BigDecimal;

public abstract class Promo {
    public final double value;
    public final BigDecimal baseProductPrice;

    public Promo(double value, BigDecimal baseProductPrice) {
        checkPromo(value, baseProductPrice);
        this.baseProductPrice = baseProductPrice;
        this.value = value;
    }

    public abstract void checkPromo(double value, BigDecimal baseProductPrice) throws IllegalArgumentException;
}

