package com.swoqe.modular;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class StreetLightMain {
    public static void main(String[] args) {
        AtomicInteger counter = new AtomicInteger();
        StreetLight streetLight = new StreetLight(counter);
        new Thread(streetLight).start();

        for (int i = 0; i < 100; i++) {
            new Thread(new Car(counter)).start();
        }
    }


}

enum Colors {
    GREEN, YELLOW, RED
}

class StreetLight implements Runnable {
    public static Colors currentLight = Colors.GREEN;
    private final AtomicInteger counter;

    public StreetLight(AtomicInteger counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        while (counter.get() < 1000) {
            try {
                TimeUnit.MILLISECONDS.sleep(40);
                currentLight = Colors.YELLOW;
                TimeUnit.MILLISECONDS.sleep(10);
                currentLight = Colors.RED;
                TimeUnit.MILLISECONDS.sleep(30);
                currentLight = Colors.YELLOW;
                TimeUnit.MILLISECONDS.sleep(10);
                currentLight = Colors.GREEN;
                System.out.println(counter.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

class Car implements Runnable {

    private final AtomicInteger counter;

    public Car(AtomicInteger counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        while (counter.get() < 1000) {
            try {
                go();
                TimeUnit.MILLISECONDS.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void go() {
        if (StreetLight.currentLight.equals(Colors.GREEN)) {
            counter.incrementAndGet();
        }
    }
}
