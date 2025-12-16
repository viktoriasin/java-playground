package ru.sinvic.multithreading.user.report.model;

import lombok.NonNull;

public record BasicUserInfo(long userId, @NonNull String name, String email) {
}
