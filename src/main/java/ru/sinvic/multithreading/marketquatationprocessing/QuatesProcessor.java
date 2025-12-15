package ru.sinvic.multithreading.marketquatationprocessing;


import lombok.NonNull;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class QuatesProcessor {

    public static final Duration WINDOW = Duration.ofMinutes(5);
    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<Trade>> tradesData =
        new ConcurrentHashMap<>();

    public Stats onTrade(@NonNull Trade trade) {
        ConcurrentLinkedDeque<Trade> tradesQueue = tradesData.computeIfAbsent(trade.symbol(), _ -> new ConcurrentLinkedDeque<>());
        tradesQueue.offer(trade);

        cleanupOldTrades(tradesQueue);

        return recomputeStats(tradesQueue);
    }

    private void cleanupOldTrades(ConcurrentLinkedDeque<Trade> tradesQueue) {
        Instant cutOff = Instant.now().minus(WINDOW);
        while (!tradesQueue.isEmpty() && tradesQueue.peek().timestamp().isBefore(cutOff)) {
            tradesQueue.poll();
        }
    }

    private Stats recomputeStats(ConcurrentLinkedDeque<Trade> tradesQueue) {
        BigDecimal avgPrice = calculateAveragePrice(tradesQueue);
        long maxPrice = calculateMaxPrice(tradesQueue);

        return new Stats(avgPrice, maxPrice);
    }

    private long calculateMaxPrice(ConcurrentLinkedDeque<Trade> tradesQueue) {
        return tradesQueue.stream()
            .map(Trade::volume)
            .max(Long::compareTo)
            .orElse(0L);
    }

    private BigDecimal calculateAveragePrice(ConcurrentLinkedDeque<Trade> tradesQueue) {
        BigDecimal sumOfTrades = tradesQueue.stream()
            .map(Trade::price)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);
        return sumOfTrades.divide(
            BigDecimal.valueOf(tradesQueue.size()), BigDecimal.ROUND_HALF_UP);
    }


}
