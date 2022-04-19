package com.swoqe.modular;

import java.util.concurrent.atomic.AtomicReference;

public class AZSwitcher {
    public static void main(String[] args) {
        Switcher switcher = new Switcher();
        A a = new A(switcher);
        B b = new B(switcher);

        new Thread(a).start();
        new Thread(b).start();
    }
}

class Switcher {
    private final AtomicReference<String> state = new AtomicReference<>("s");

    public void startCount() throws InterruptedException {
        int i = 100;
        while (i != 0) {
            if (state.get().equals("s")) {
                System.out.println(i--);
                Thread.sleep(500);
            } else {
                Thread.yield();
            }
        }
    }

    public synchronized void changeState() throws InterruptedException {
        state.set(state.get().equals("s") ? "z" : "s");
    }

}

class A implements Runnable {

    private final Switcher switcher;

    public A(Switcher switcher) {
        this.switcher = switcher;
    }


    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
                switcher.changeState();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class B implements Runnable {
    private final Switcher switcher;

    public B(Switcher switcher) {
        this.switcher = switcher;
    }

    public void run() {
        while (true) {
            try {
                switcher.startCount();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}