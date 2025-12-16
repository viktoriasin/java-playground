package ru.sinvic.multithreading.employee.report.service;

import ru.sinvic.multithreading.employee.report.model.EmploymentInfo;

import java.util.concurrent.CompletableFuture;

public interface EmploymentInfoService {
    CompletableFuture<EmploymentInfo> fetchEmploymentData(long id);
}
