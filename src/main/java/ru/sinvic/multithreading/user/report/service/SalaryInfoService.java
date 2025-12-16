package ru.sinvic.multithreading.user.report.service;

import ru.sinvic.multithreading.user.report.model.SalaryInfo;

import java.util.concurrent.CompletableFuture;

public interface SalaryInfoService {
    CompletableFuture<SalaryInfo> fetchSalaryInfo(long id);
}
