package ru.sinvic.multithreading.market.quatation.processing;

import lombok.NonNull;

import java.math.BigDecimal;

public record Stats(@NonNull BigDecimal avgPriceForFiveMinutes,
                    long maxVolumeFor5Minutes) {
}
