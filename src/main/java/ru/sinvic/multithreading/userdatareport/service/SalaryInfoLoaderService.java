package ru.sinvic.multithreading.userdatareport.service;

import ru.sinvic.multithreading.userdatareport.model.UserSalaryInfo;

import java.util.concurrent.CompletableFuture;

public interface SalaryInfoLoaderService {
    CompletableFuture<UserSalaryInfo> fetchSalaryInfo(long id);
}
