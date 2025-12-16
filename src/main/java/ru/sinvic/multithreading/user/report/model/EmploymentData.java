package ru.sinvic.multithreading.user.report.model;

import lombok.NonNull;

public record EmploymentData(long userId,
                             String position,
                             String department) {
}
