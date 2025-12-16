package ru.sinvic.multithreading.employee.report.model;

import ru.sinvic.multithreading.employee.report.exceptions.UserInfoException;

public record UserInfoResult(UserInfo userInfo, UserInfoException userInfoError) {}
