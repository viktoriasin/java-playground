package ru.sinvic.multithreading.employee.report.model;

import lombok.NonNull;
import ru.sinvic.multithreading.employee.report.exceptions.SalaryInfoException;

import java.math.BigDecimal;

public record SalaryInfo(long userId, @NonNull BigDecimal salary) {
}

