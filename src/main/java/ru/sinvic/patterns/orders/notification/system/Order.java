package ru.sinvic.patterns.orders.notification.system;

import lombok.NonNull;

import java.time.LocalDateTime;

public record Order(long id, @NonNull String description, @NonNull LocalDateTime createdAt) {
}
