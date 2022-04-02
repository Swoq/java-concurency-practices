package com.swoqe.first;

import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=========== Symbols Test ===========");
        CharPrinter charPrinter = new CharPrinter();

        Thread threadPipe = new Thread(() -> charPrinter.run('-'));
        Thread threadDash = new Thread(() -> charPrinter.run('|'));
        threadPipe.start();
        threadDash.start();

        threadDash.join();
        threadPipe.join();
        System.out.println("\n=========== Counter Test ===========");
        LockingCounter myCounter = new LockingCounter();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                myCounter.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                myCounter.decrement();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println("Counter after threads' processing: " + myCounter.getCounter());
    }
}

class CharPrinter {

    private static final int N = 1000;
    private volatile boolean dashTurn = true;

    public synchronized void run(char symbol) {
        for (int i = 0; i < N; i++) {
            while (dashTurn && symbol == '-') {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (!dashTurn && symbol == '|') {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.print(symbol);
            dashTurn = !dashTurn;
            notifyAll();
        }
    }

}

class LockingCounter {
    ReentrantLock lock = new ReentrantLock();
    private long counter = 0;

    public void increment() {
        lock.lock();
        counter++;
        lock.unlock();
    }

    public void decrement() {
        lock.lock();
        counter--;
        lock.unlock();
    }

    public long getCounter() {
        try {
            lock.lock();
            return counter;
        } finally {
            lock.unlock();
        }
    }
}


