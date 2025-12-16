package ru.sinvic.multithreading.employee.report.service;

import ru.sinvic.multithreading.employee.report.exceptions.UserInfoException;
import ru.sinvic.multithreading.employee.report.model.UserInfo;

import java.util.concurrent.CompletableFuture;

public interface UserInfoService {
    CompletableFuture<UserInfo> fetchBasicInfo(long id);
}
