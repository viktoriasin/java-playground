package ru.sinvic.multithreading.employee.report.model;

import lombok.NonNull;
import ru.sinvic.multithreading.employee.report.exceptions.UserInfoException;

public record UserInfo(long userId, @NonNull String name, String email) {
    public UserInfo {
        if(name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
    }
}

;
