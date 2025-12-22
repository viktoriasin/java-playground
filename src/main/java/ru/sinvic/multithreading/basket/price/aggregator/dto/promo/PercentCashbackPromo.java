package ru.sinvic.multithreading.basket.price.aggregator.dto.promo;

import java.math.BigDecimal;

public class PercentCashbackPromo extends PromoCashBack {
    public PercentCashbackPromo(double value, BigDecimal baseProductPrice) {
        super(value, baseProductPrice);
    }

    @Override
    public void checkPromo(double value, BigDecimal baseProductPrice) throws IllegalArgumentException {
        if (value <= 0 || value >= 100) {
            throw new IllegalArgumentException(STR."ChashBack percent value \{value} incorrect");
        }
    }

    @Override
    public BigDecimal getCashBack(BigDecimal priceWithoutDiscount) {
        return priceWithoutDiscount.multiply(BigDecimal.valueOf(value));
    }

}
