package ru.sinvic.multithreading.user.report.model;

import lombok.NonNull;

import java.math.BigDecimal;

public record UserSalaryInfo(long userId, BigDecimal salary) {
}
