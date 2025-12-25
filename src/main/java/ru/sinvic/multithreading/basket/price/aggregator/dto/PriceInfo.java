package ru.sinvic.multithreading.basket.price.aggregator.dto;

import lombok.NonNull;
import ru.sinvic.multithreading.basket.price.aggregator.dto.promo.Promo;
import ru.sinvic.multithreading.basket.price.aggregator.dto.promo.PromoCashBack;

import java.math.BigDecimal;
import java.util.List;

public record PriceInfo(long productId, BigDecimal basePrice, List<Promo> promoTypes) {
    public PriceInfo {
        if (basePrice == null) {
            throw new IllegalArgumentException("BasePrice cannot be null");
        }
        if (promoTypes == null) {
            throw new IllegalArgumentException("PromoTypes cannot be null");
        }
        checkCashBackPromoQuantity(promoTypes);
        checkBasePrice(basePrice);
    }

    private void checkCashBackPromoQuantity(@NonNull List<Promo> promoTypes) {
        long cashBackPromoCount = promoTypes.stream()
            .filter(promo -> promo instanceof PromoCashBack)
            .count();
        if (cashBackPromoCount > 1) {
            throw new IllegalArgumentException("There can only be one cash back promo!");
        }
    }

    private void checkBasePrice(@NonNull BigDecimal basePrice) {
        if (basePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(STR."Base price can not be less than or equal to zero! Get: \{basePrice}");
        }
    }
}
