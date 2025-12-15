package ru.sinvic.datamanipulation.memorycachewithttl;

import lombok.NonNull;

public interface Cache {
    void put(@NonNull String key, Integer value);

    Integer get(@NonNull String key);
}
