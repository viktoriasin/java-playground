package ru.sinvic.multithreading.marketquatationprocessing;

import lombok.NonNull;

import java.math.BigDecimal;

public record Stats(@NonNull BigDecimal avgPriceForFiveMinutes,
                    long maxVolumeFor5Minutes) {
}
