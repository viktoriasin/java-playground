package ru.sinvic.multithreading.userdatareport.model;

import lombok.NonNull;

import java.math.BigDecimal;

public record UserSalaryInfo(long userId, @NonNull BigDecimal salary) {
}
