package ru.sinvic.multithreading.basket.price.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sinvic.multithreading.basket.price.aggregator.dto.BasketItem;
import ru.sinvic.multithreading.basket.price.aggregator.dto.PriceInfo;
import ru.sinvic.multithreading.basket.price.aggregator.dto.promo.Promo;
import ru.sinvic.multithreading.basket.price.aggregator.dto.promo.PromoCashBack;
import ru.sinvic.multithreading.basket.price.aggregator.dto.promo.PromoDiscount;
import ru.sinvic.multithreading.basket.price.aggregator.exception.BasketException;
import ru.sinvic.multithreading.basket.price.aggregator.model.BasketResult;
import ru.sinvic.multithreading.basket.price.aggregator.model.BasketTotalPriceInfo;
import ru.sinvic.multithreading.basket.price.aggregator.service.PriceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class BasketPriceAggregator {

    private final PriceService priceService;

    public CompletableFuture<BasketResult> calculateCart(List<BasketItem> items, long basketId) {
        checkBasket(items.size());

        List<CompletableFuture<ItemFinalPriceInfo>> itemsFuture = items.stream().map(this::calculateItemPrice).toList();
        CompletableFuture<List<ItemFinalPriceInfo>> listCompletableFuture = CompletableFuture.allOf(itemsFuture.toArray(new CompletableFuture[0]))
            .thenApply(_ -> itemsFuture.stream()
                .map(CompletableFuture::join)
                .toList());
        return listCompletableFuture.thenApply(finalItemPriceAndCashBack -> getBasketTotalPriceInfo(finalItemPriceAndCashBack, basketId))
            .orTimeout(2, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                log.error(STR."Calculation failed: \{ex.getMessage()}");
                return new BasketResult(null, (Exception) ex);
            });
    }

    private BasketResult getBasketTotalPriceInfo(List<ItemFinalPriceInfo> itemFinalPriceInfoList, long basketId) {
        BigDecimal totalBasketPrice = BigDecimal.ZERO;
        BigDecimal cashBack = BigDecimal.ZERO;
        for (ItemFinalPriceInfo itemFinalPriceInfo : itemFinalPriceInfoList) {
            BigDecimal priceWithDiscounts = itemFinalPriceInfo.priceWithDiscounts;
            totalBasketPrice = totalBasketPrice.add(priceWithDiscounts);
            cashBack = cashBack.add(itemFinalPriceInfo.cashBack.orElse(BigDecimal.ZERO));
        }
        Optional<BigDecimal> cashBackOptional;

        if (cashBack.compareTo(BigDecimal.ZERO) == 0) {
            cashBackOptional = Optional.empty();
        } else {
            cashBackOptional = Optional.of(cashBack);
        }
        return new BasketResult(new BasketTotalPriceInfo(basketId, totalBasketPrice, cashBackOptional), null);
    }

    private CompletableFuture<ItemFinalPriceInfo> calculateItemPrice(BasketItem item) {
        return CompletableFuture.supplyAsync(() -> {
            PriceInfo priceInfo = null;
            try {
                priceInfo = priceService.getPriceInfo(item.productId());
                BigDecimal itemPriceWithDiscounts = calculateItemPriceWithDiscounts(item, priceInfo);
                Optional<BigDecimal> cashBack = calculateCashBack(itemPriceWithDiscounts, priceInfo.promoTypes());
                return new ItemFinalPriceInfo(itemPriceWithDiscounts, cashBack);
            } catch (Exception e) {
                log.error("Can not calculate price for item {}", item);
                return new ItemFinalPriceInfo(BigDecimal.ZERO, Optional.empty());
            }
        });
    }

    private Optional<BigDecimal> calculateCashBack(BigDecimal itemPriceWithDiscounts, List<Promo> promos) {
        for (Promo promo : promos) {
            if (promo instanceof PromoCashBack promoCashBack) {
                return Optional.of(promoCashBack.getCashBack(itemPriceWithDiscounts));
            }
        }
        return Optional.empty();
    }

    private BigDecimal calculateItemPriceWithDiscounts(BasketItem item, PriceInfo priceInfo) {
        BigDecimal totalPrice = priceInfo.basePrice().multiply(BigDecimal.valueOf(item.quantity()));

        return applyDiscounts(totalPrice, priceInfo.promoTypes());
    }

    private BigDecimal applyDiscounts(BigDecimal totalPrice, List<Promo> promos) {
        BigDecimal priceWithDiscount = totalPrice;
        for (Promo promo : promos) {
            if (promo instanceof PromoDiscount promoDiscount) {
                priceWithDiscount = promoDiscount.applyPromo(priceWithDiscount);
            }
        }
        return priceWithDiscount;
    }

    private void checkBasket(int size) {
        if (size < 1 || size > 1000) {
            throw new BasketException(STR."Basket size must not be greater than 1000 or less than 1!Get \{size}");
        }
    }

    private record ItemFinalPriceInfo(BigDecimal priceWithDiscounts,
                                      Optional<BigDecimal> cashBack) {
    }
}
