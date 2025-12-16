package ru.sinvic.multithreading.employee.report.model;

import lombok.NonNull;
import ru.sinvic.multithreading.employee.report.exceptions.EmploymentInfoException;

public record EmploymentInfo(long userId,
                             @NonNull String position,
                             @NonNull String department) {
}

