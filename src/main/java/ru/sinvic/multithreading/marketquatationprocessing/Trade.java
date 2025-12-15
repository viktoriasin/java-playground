package ru.sinvic.multithreading.marketquatationprocessing;

import lombok.NonNull;

import java.math.BigDecimal;
import java.time.Instant;

public record Trade(@NonNull String symbol, @NonNull BigDecimal price,
                    @NonNull Long volume, @NonNull Instant timestamp) {
    public Trade {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be greater or equal to zero");
        }
    }
}
