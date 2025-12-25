package ru.sinvic.multithreading.basket.price.aggregator;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sinvic.multithreading.basket.price.aggregator.dto.BasketItem;
import ru.sinvic.multithreading.basket.price.aggregator.dto.PriceInfo;
import ru.sinvic.multithreading.basket.price.aggregator.model.BasketResult;
import ru.sinvic.multithreading.basket.price.aggregator.model.BasketTotalPriceInfo;
import ru.sinvic.multithreading.basket.price.aggregator.service.PriceService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class BasketPriceAggregatorTest {

    @Test
    public void testParallelProcessingWithTimeout() throws Exception {
        PriceService priceService = productId -> {
            long delay = 100 + (long) (Math.random() * 300);

            Thread.sleep(delay);

            return new PriceInfo(1L, BigDecimal.TEN, Collections.emptyList());
        };

        List<BasketItem> basketItems = LongStream.range(0, 50)
            .mapToObj(i -> new BasketItem(i, 1L, 1))
            .toList();

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        BasketPriceAggregator basketPriceAggregator = new BasketPriceAggregator(priceService, executorService);

        CompletableFuture<BasketResult> basketResultCompletableFuture = basketPriceAggregator.calculateCart(basketItems, 1L);

        try {
            BasketResult basketResult = basketResultCompletableFuture.get(2, TimeUnit.SECONDS);

            assertNull(basketResult.ex());
            assertNotNull(basketResult.basketTotalPriceInfo());
            BasketTotalPriceInfo basketTotalPriceInfo = basketResult.basketTotalPriceInfo();
            assertEquals(basketTotalPriceInfo.priceWIthDiscounts().compareTo(BigDecimal.valueOf(500L)), 0);
            assertTrue(basketTotalPriceInfo.cashBack().isEmpty());
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
}