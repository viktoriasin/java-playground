package ru.sinvic.multithreading.basket.price.aggregator.dto;

import java.math.BigDecimal;

public class PercentCashbackPromo extends PercentDiscountPromo {
    public PercentCashbackPromo(double value, BigDecimal baseProductPrice) {
        super(value, baseProductPrice);
    }
}
