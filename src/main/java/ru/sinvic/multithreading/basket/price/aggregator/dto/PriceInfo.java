package ru.sinvic.multithreading.basket.price.aggregator.dto;

import lombok.NonNull;
import ru.sinvic.multithreading.basket.price.aggregator.dto.promo.Promo;
import ru.sinvic.multithreading.basket.price.aggregator.dto.promo.PromoCashBack;

import java.math.BigDecimal;
import java.util.List;

public record PriceInfo(long productId, @NonNull BigDecimal basePrice, @NonNull List<Promo> promoTypes) {
    public PriceInfo {
        checkCashBackPromoQuantity();
        checkBasePrice();
    }

    private void checkCashBackPromoQuantity() {
        long cashBackPromoCount = promoTypes.stream()
            .filter(promo -> promo instanceof PromoCashBack)
            .count();
        if (cashBackPromoCount > 1) {
            throw new IllegalArgumentException("There can only be one cash back promo!");
        }
    }

    private void checkBasePrice() {
        if (basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(STR."Base price can not be less than or equal to zero! Get: \{basePrice}");
        }
    }
}
