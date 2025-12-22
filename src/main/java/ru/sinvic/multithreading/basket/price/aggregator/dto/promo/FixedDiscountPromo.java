package ru.sinvic.multithreading.basket.price.aggregator.dto.promo;

import java.math.BigDecimal;

public class FixedDiscountPromo extends PromoDiscount {
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

    @Override
    public BigDecimal applyPromo(BigDecimal priceWithoutDiscount) {
        return priceWithoutDiscount.subtract(BigDecimal.valueOf(value));
    }
}
