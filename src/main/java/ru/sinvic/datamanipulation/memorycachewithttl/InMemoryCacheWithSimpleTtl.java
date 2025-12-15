package ru.sinvic.datamanipulation.memorycachewithttl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class InMemoryCacheWithSimpleTtl implements Cache {
    private final long ttlInSecond;

    private final ConcurrentHashMap<String, CacheItem> cache = new ConcurrentHashMap<>();


    @Override
    public void put(@NonNull String key, @NonNull Integer value) {
        cache.put(key, new CacheItem(value, Instant.now().plus(Duration.ofSeconds(ttlInSecond))));
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

    private record CacheItem(@NonNull Integer value, @NonNull Instant expirationTime) {

        public boolean isExpired() {
            return Instant.now().isAfter(expirationTime);
        }
    }
}
