package ru.sinvic.datamanipulation.memorycachewithttl;


public interface ClosableCache extends Cache, AutoCloseable {
    @Override
    void close() throws InterruptedException;
}
