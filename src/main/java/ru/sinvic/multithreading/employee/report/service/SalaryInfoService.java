package ru.sinvic.multithreading.employee.report.service;

import ru.sinvic.multithreading.employee.report.model.SalaryInfo;

import java.util.concurrent.CompletableFuture;

public interface SalaryInfoService {
    CompletableFuture<SalaryInfo> fetchSalaryInfo(long id);
}
