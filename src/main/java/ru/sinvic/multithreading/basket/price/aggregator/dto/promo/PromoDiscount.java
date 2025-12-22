package ru.sinvic.multithreading.basket.price.aggregator.dto.promo;

import java.math.BigDecimal;

public abstract class PromoDiscount extends Promo {
    public PromoDiscount(double value, BigDecimal baseProductPrice) {
        super(value, baseProductPrice);
    }

    public abstract BigDecimal applyPromo(BigDecimal priceWithoutDiscount);
}
