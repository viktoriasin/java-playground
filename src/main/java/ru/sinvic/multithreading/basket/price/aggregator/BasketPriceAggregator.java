package ru.sinvic.multithreading.basket.price.aggregator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sinvic.multithreading.basket.price.aggregator.dto.BasketItem;
import ru.sinvic.multithreading.basket.price.aggregator.dto.PriceInfo;
import ru.sinvic.multithreading.basket.price.aggregator.dto.promo.Promo;
import ru.sinvic.multithreading.basket.price.aggregator.dto.promo.PromoCashBack;
import ru.sinvic.multithreading.basket.price.aggregator.dto.promo.PromoDiscount;
import ru.sinvic.multithreading.basket.price.aggregator.exception.BasketException;
import ru.sinvic.multithreading.basket.price.aggregator.model.BasketTotalPriceInfo;
import ru.sinvic.multithreading.basket.price.aggregator.service.PriceService;
import ru.sinvic.multithreading.basket.price.aggregator.service.TaxService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
public class BasketPriceAggregator {

    private final PriceService priceService;
    private final TaxService taxService;

    public CompletableFuture<BasketTotalPriceInfo> calculateCart(List<BasketItem> items) {
        checkBasket(items.size());

        List<CompletableFuture<ItemFinalPriceInfo>> list = items.stream().map(this::calculateItemPrice).toList();
        return null;
    }

    private CompletableFuture<ItemFinalPriceInfo> calculateItemPrice(BasketItem item) {
        return CompletableFuture.supplyAsync(() -> {
            PriceInfo priceInfo = null;
            try {
                priceInfo = priceService.getPriceInfo(item.productId());
                BigDecimal finalPriceWithDiscounts = calculateItemPriceWithDiscounts(item, priceInfo);
                Optional<BigDecimal> cashBack = calculateCashBack(finalPriceWithDiscounts, priceInfo.promoTypes());
                return new ItemFinalPriceInfo(finalPriceWithDiscounts, cashBack);
            } catch (Exception e) {
                log.error("Can not calculate price for item {}", item);
                return new ItemFinalPriceInfo(BigDecimal.ZERO, Optional.empty());
            }
        });
    }

    private Optional<BigDecimal> calculateCashBack(BigDecimal finalPriceWithDiscounts, List<Promo> promos) {
        for (Promo promo : promos) {
            if (promo instanceof PromoCashBack promoCashBack) {
                return Optional.of(promoCashBack.getCashBack(finalPriceWithDiscounts));
            }
        }
        return Optional.empty();
    }

    private BigDecimal calculateItemPriceWithDiscounts(BasketItem item, PriceInfo priceInfo) {
        BigDecimal totalPrice = priceInfo.basePrice().multiply(BigDecimal.valueOf(item.quantity()));

        return applyDiscounts(totalPrice, priceInfo.promoTypes());
    }

    private BigDecimal applyDiscounts(BigDecimal totalPrice, @NonNull List<Promo> promos) {
        BigDecimal priceWithDiscount = totalPrice;
        for (Promo promo : promos) {
            if (promo instanceof PromoDiscount promoDiscount) {
                priceWithDiscount = promoDiscount.applyPromo(priceWithDiscount);
            }
        }
        return priceWithDiscount;
    }

    private static void checkBasket(int size) {
        if (size > 1000) {
            throw new BasketException(STR."Basket size must not be greater than 1000!Get \{size}")
        }
    }

    private record ItemFinalPriceInfo(@NonNull BigDecimal priceWIthDiscounts,
                                      Optional<BigDecimal> cashBack) {
    }

    ;
}
