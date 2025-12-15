package ru.sinvic.multithreading.userdatareport.service;

import ru.sinvic.multithreading.userdatareport.model.EmploymentData;

import java.util.concurrent.CompletableFuture;

public interface EmploymentDataLoaderService {
    CompletableFuture<EmploymentData> fetchEmploymentData(long id);
}
