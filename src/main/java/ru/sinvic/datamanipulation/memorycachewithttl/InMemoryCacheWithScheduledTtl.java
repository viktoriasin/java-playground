package ru.sinvic.datamanipulation.memorycachewithttl;

import lombok.NonNull;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InMemoryCacheWithScheduledTtl implements ClosableCache {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(InMemoryCacheWithScheduledTtl.class);
    private final long ttlInSecond;
    private final ConcurrentHashMap<String, CacheItem> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupScheduler = Executors.newSingleThreadScheduledExecutor();

    public InMemoryCacheWithScheduledTtl(long ttlInSecond) {
        if (ttlInSecond <= 0) {
            throw new IllegalArgumentException("TTL must be positive");
        }
        this.ttlInSecond = ttlInSecond;

        long cleanupInterval = Math.max(ttlInSecond / 10, 1);
        cleanupScheduler.scheduleAtFixedRate(this::cleanupExpiredItems,
            cleanupInterval, cleanupInterval, TimeUnit.SECONDS);
    }

    @Override
    public void put(@NonNull String key, @NonNull Integer value) {
        cache.put(key,
            new CacheItem(value, Instant.now().plus(Duration.ofSeconds(ttlInSecond))));
    }

    @Override
    public Integer get(@NonNull String key) {
        CacheItem cacheItem = cache.get(key);

        if (cacheItem == null) {
            return null;
        }

        if (cacheItem.isExpired()) {
            cache.remove(key);
            return null;
        }
        return cacheItem.value;
    }

    public int size() {
        return cache.size();
    }

    public void close() throws InterruptedException {
        cleanupScheduler.shutdown();
        if (!cleanupScheduler.awaitTermination(60, TimeUnit.SECONDS)) {
            cleanupScheduler.shutdownNow();
        }
    }

    private void cleanupExpiredItems() {
        for (var entry : cache.entrySet()) {
            CacheItem value = entry.getValue();
            String key = entry.getKey();
            if (value.isExpired()) {
                log.info("Removed expired entry {} {}", key, value);
                cache.remove(key);
            }
        }
    }

    private record CacheItem(@NonNull Integer value, @NonNull Instant expiresAt) {

        public boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }
}
