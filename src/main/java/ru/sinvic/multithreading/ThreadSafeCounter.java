package ru.sinvic.multithreading;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSafeCounter {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int getValue() {
        return count.get();
    }

    public void reset() {
        count.getAndSet(0);
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadSafeCounter counter = new ThreadSafeCounter();
        Thread[] threads = new Thread[10];

        Runnable task = () -> {

            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        };

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }

        for (int i = 0; i < 10; i++) {
            threads[i].join();
        }

        System.out.println(counter.getValue());

    }
}
