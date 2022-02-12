package com.swoqe.first;

public class TwoThreads {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=========== Symbols Test ===========");
        Thread threadPipe = new Thread(() -> printSymbolThousandTimes('|'));
        threadPipe.start();
        Thread threadDash = new Thread(() -> printSymbolThousandTimes('-'));
        threadDash.start();

        threadDash.join();
        threadPipe.join();
        System.out.println("=========== Counter Test ===========");
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                MyCounter.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                MyCounter.decrement();
            }
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println(MyCounter.getCounter());

    }

    private synchronized static void printSymbolThousandTimes(char symbol) {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(symbol);
            }
            System.out.println();
        }
    }

    static class MyCounter {
        private static long counter = 0;

//        private final Object lock = new Object();

        public synchronized static void increment() {
//        synchronized (MyCounter.class) or synchronized (lock)
            counter++;
        }

        public synchronized static void decrement() {
            counter--;
        }

        public synchronized static long getCounter() {
            return counter;
        }
    }
}


