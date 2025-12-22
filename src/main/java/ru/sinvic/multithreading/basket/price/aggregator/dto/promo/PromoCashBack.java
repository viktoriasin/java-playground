package ru.sinvic.multithreading.basket.price.aggregator.dto.promo;

import java.math.BigDecimal;

public abstract class PromoCashBack extends Promo {
    public PromoCashBack(double value, BigDecimal baseProductPrice) {
        super(value, baseProductPrice);
    }

    public abstract BigDecimal getCashBack(BigDecimal priceWithoutDiscount);
}
