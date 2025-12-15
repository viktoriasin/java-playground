package ru.sinvic.multithreading.userdatareport.service;

import ru.sinvic.multithreading.userdatareport.BasicUserInfoLoadingException;
import ru.sinvic.multithreading.userdatareport.model.BasicUserInfo;

import java.util.concurrent.CompletableFuture;

public interface BasicUserInfoLoaderService {
    CompletableFuture<BasicUserInfo> fetchBasicInfo(long id) throws BasicUserInfoLoadingException;
}
