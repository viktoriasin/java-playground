package ru.sinvic.multithreading.userdatareport.model;

import lombok.NonNull;

public record EmploymentData(long userId,
                             String position,
                             String department) {
}
