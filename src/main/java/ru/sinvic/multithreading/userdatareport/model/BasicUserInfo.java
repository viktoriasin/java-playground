package ru.sinvic.multithreading.userdatareport.model;

import lombok.NonNull;

public record BasicUserInfo(long userId, @NonNull String name, String email) {
}
