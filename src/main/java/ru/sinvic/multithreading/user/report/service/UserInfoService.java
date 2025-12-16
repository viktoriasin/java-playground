package ru.sinvic.multithreading.user.report.service;

import ru.sinvic.multithreading.user.report.BasicUserInfoLoadingException;
import ru.sinvic.multithreading.user.report.model.UserInfo;

import java.util.concurrent.CompletableFuture;

public interface UserInfoService {
    CompletableFuture<UserInfo> fetchBasicInfo(long id) throws BasicUserInfoLoadingException;
}
