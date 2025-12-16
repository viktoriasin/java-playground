package ru.sinvic.multithreading.user.report.service;

import ru.sinvic.multithreading.user.report.model.EmploymentInfo;

import java.util.concurrent.CompletableFuture;

public interface EmploymentInfoService {
    CompletableFuture<EmploymentInfo> fetchEmploymentData(long id);
}
