package com.swoqe.third;

import java.util.Random;

public class ProducerConsumerMain {
    public static void main(String[] args) {
        Drop drop = new Drop();
        Producer producer = new Producer(drop, 5000);
        Consumer consumer = new Consumer(drop);

        new Thread(producer).start();
        new Thread(consumer).start();
    }
}

class Drop {
    // Message sent from producer
    // to consumer.
    private int message;

    // True if consumer should wait
    // for producer to send message,
    // false if producer should wait for
    // consumer to retrieve message.
    private boolean empty = true;

    public synchronized int take() {
        // Wait until message is
        // available.
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Toggle status.
        empty = true;
        // Notify producer that
        // status has changed.
        notifyAll();
        return message;
    }

    public synchronized void put(int message) {
        // Wait until message has
        // been retrieved.
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Toggle status.
        empty = false;
        // Store message.
        this.message = message;
        // Notify consumer that status
        // has changed.
        notifyAll();
    }

}

class Producer implements Runnable {

    private final Drop drop;
    private final long size;

    public Producer(Drop drop, long size) {
        this.drop = drop;
        this.size = size;
    }


    public void run() {
        Random random = new Random();
        int[] importantInfo = random.ints(this.size).toArray();

        for (int s : importantInfo) {
            drop.put(s);
            try {
                Thread.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        drop.put(Integer.MAX_VALUE);
    }
}

class Consumer implements Runnable {
    private final Drop drop;

    public Consumer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        Random random = new Random();
        for (int message = drop.take(); message != Integer.MAX_VALUE; message = drop.take()) {
            System.out.format("MESSAGE RECEIVED: %s%n", message);
            try {
                Thread.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}