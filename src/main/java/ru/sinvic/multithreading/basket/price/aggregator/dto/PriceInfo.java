package ru.sinvic.multithreading.basket.price.aggregator.dto;

import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

public record PriceInfo(long productId, @NonNull BigDecimal basePrice, @NonNull List<Promo> promoTypes) {
}
