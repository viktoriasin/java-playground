package ru.sinvic.multithreading.basket.price.aggregator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sinvic.multithreading.basket.price.aggregator.dto.BasketItem;
import ru.sinvic.multithreading.basket.price.aggregator.dto.PriceInfo;
import ru.sinvic.multithreading.basket.price.aggregator.model.BasketResult;
import ru.sinvic.multithreading.basket.price.aggregator.model.BasketTotalPriceInfo;
import ru.sinvic.multithreading.basket.price.aggregator.service.PriceService;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class BasketPriceAggregatorTest {

    private List<BasketItem> basketItems;

    @BeforeEach
    public void beforeEach() {
        basketItems = LongStream.range(0, 50)
            .mapToObj(i -> new BasketItem(i, 1L, 1))
            .toList();
    }

    @Test
    void testParallelProcessingWithTimeout() throws Exception {
        PriceService priceService = mockPriceService(false, false);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        BasketPriceAggregator basketPriceAggregator = new BasketPriceAggregator(priceService, executorService);
        CompletableFuture<BasketResult> basketResultCompletableFuture = basketPriceAggregator.calculateCart(basketItems, 1L);

        try {
            BasketResult basketResult = basketResultCompletableFuture.get(2, TimeUnit.SECONDS);
            basicAssertionsOnBasketResult(basketResult, BigDecimal.valueOf(500L));
        } catch (TimeoutException e) {
            fail(STR."Расчёт не уложился в 2 секунды: \{e.getMessage()}");
        } catch (ExecutionException e) {
            fail(STR."Ошибка при расчёте корзины: \{e.getCause().getMessage()}");
        } finally {
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        }
    }

    @Test
    void testParallelProcessingWithIncorrectPriceInfoData()  throws Exception {
        PriceService priceService = mockPriceService(true, false);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        BasketPriceAggregator basketPriceAggregator = new BasketPriceAggregator(priceService, executorService);
        CompletableFuture<BasketResult> basketResultCompletableFuture = basketPriceAggregator.calculateCart(basketItems, 1L);

        try {
            BasketResult basketResult = basketResultCompletableFuture.get(2, TimeUnit.SECONDS);
            basicAssertionsOnBasketResult(basketResult, BigDecimal.valueOf(480));
        } catch (TimeoutException e) {
            fail(STR."Расчёт не уложился в 2 секунды: \{e.getMessage()}");
        } catch (ExecutionException e) {
            fail(STR."Ошибка при расчёте корзины: \{e.getCause().getMessage()}");
        } finally {
            executorService.shutdown();
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        }
    }

    @Test
    void testPriceServiceTimeout() {
        PriceService priceService = mockPriceService(false, true);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        BasketPriceAggregator basketPriceAggregator = new BasketPriceAggregator(priceService, executorService);

        assertTimeout(Duration.ofMillis(2000),
            () -> basketPriceAggregator.calculateCart(basketItems, 1L));

        executorService.shutdown();
    }

    private void basicAssertionsOnBasketResult(BasketResult basketResult, BigDecimal expectedFinalPrice) {
        assertNull(basketResult.ex());
        assertNotNull(basketResult.basketTotalPriceInfo());
        BasketTotalPriceInfo basketTotalPriceInfo = basketResult.basketTotalPriceInfo();
        assertEquals(basketTotalPriceInfo.priceWIthDiscounts().compareTo(expectedFinalPrice), 0);
        assertTrue(basketTotalPriceInfo.cashBack().isEmpty());
    }

    private PriceService mockPriceService(boolean withIncorrectPriceInfo, boolean withLongDelay) {
        // для точного контроля, сколько товаров не будут учтены в финальной цене
        AtomicInteger countIncorrectPriceInfoInstance = new AtomicInteger(2);
        return _ -> {
            long delay = 100 + (long) (Math.random() * 300);

            if (withIncorrectPriceInfo && countIncorrectPriceInfoInstance.get() > 0) {
                    countIncorrectPriceInfoInstance.getAndDecrement();
                    return new PriceInfo(1L, BigDecimal.ZERO, Collections.emptyList());
            }

            if (withLongDelay) {
                Thread.sleep(3000);
            } else {
                Thread.sleep(delay);
            }
            return new PriceInfo(1L, BigDecimal.TEN, Collections.emptyList());
        };
    }
}