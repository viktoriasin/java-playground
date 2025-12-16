package ru.sinvic.multithreading.user.report.model;

import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EmployeeReport(@NonNull String name,
                             String email,
                             String position,
                             String department,
                             BigDecimal salary,
                             @NonNull LocalDateTime fetchedAt) {
}
