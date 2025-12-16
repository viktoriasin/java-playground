package ru.sinvic.multithreading.user.report.service;

import ru.sinvic.multithreading.user.report.model.UserSalaryInfo;

import java.util.concurrent.CompletableFuture;

public interface SalaryInfoService {
    CompletableFuture<UserSalaryInfo> fetchSalaryInfo(long id);
}
