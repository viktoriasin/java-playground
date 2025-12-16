package ru.sinvic.multithreading.user.report.service;

import ru.sinvic.multithreading.user.report.model.EmploymentData;

import java.util.concurrent.CompletableFuture;

public interface EmploymentDataService {
    CompletableFuture<EmploymentData> fetchEmploymentData(long id);
}
