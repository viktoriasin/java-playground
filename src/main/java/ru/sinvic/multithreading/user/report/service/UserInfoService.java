package ru.sinvic.multithreading.user.report.service;

import ru.sinvic.multithreading.user.report.BasicUserInfoLoadingException;
import ru.sinvic.multithreading.user.report.model.BasicUserInfo;

import java.util.concurrent.CompletableFuture;

public interface UserInfoService {
    CompletableFuture<BasicUserInfo> fetchBasicInfo(long id) throws BasicUserInfoLoadingException;
}
