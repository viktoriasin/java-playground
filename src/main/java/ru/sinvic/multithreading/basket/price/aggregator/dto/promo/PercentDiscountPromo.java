package ru.sinvic.multithreading.basket.price.aggregator.dto.promo;

import java.math.BigDecimal;

public class PercentDiscountPromo extends PromoDiscount {
    public PercentDiscountPromo(double value, BigDecimal baseProductPrice) {
        super(value, baseProductPrice);
    }

    @Override
    public void checkPromo(double value, BigDecimal baseProductPrice) throws IllegalArgumentException {
        if (value <= 0 || value >= 100) {
            throw new IllegalArgumentException(STR."Discount value \{value} incorrect");
        }
    }

    @Override
    public BigDecimal applyPromo(BigDecimal priceWithoutDiscount) {
        return priceWithoutDiscount.multiply(BigDecimal.valueOf(1 - value));
    }
}
