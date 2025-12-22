package ru.sinvic.multithreading.basket.price.aggregator.dto;

import java.math.BigDecimal;

public class FixedDiscountPromo extends Promo {
    private final BigDecimal baseProductPrice;

    public FixedDiscountPromo(double value, BigDecimal baseProductPrice) {
        super(value, baseProductPrice);
        this.baseProductPrice = baseProductPrice;
    }

    @Override
    public void checkPromo(double value, BigDecimal baseProductPrice) throws IllegalArgumentException {
        if (value <= 0 || value >= baseProductPrice.doubleValue()) {
            throw new IllegalArgumentException(STR."Fixed discount value \{value} incorrect");
        }
    }
}
