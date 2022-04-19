package com.swoqe.modular;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TextWriters {
    public static void main(String[] args) {
        Text text = new Text();

        List<Thread> writers = List.of(
                new Thread(new Writer(text, List.of("1", "2", "3"))),
                new Thread(new Writer(text, List.of("4", "5", "6"))),
                new Thread(new Writer(text, List.of("7", "8", "9"))),
                new Thread(new Writer(text, List.of("10", "11", "12")))
        );
        writers.forEach(Thread::start);
        writers.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        text.show();
    }
}

class Text {
    private final List<String> strings = new ArrayList<>();

    public synchronized void addText(String text) {
        strings.add(text);
    }

    public void show() {
        System.out.println(strings);
    }
}

class Writer implements Runnable {

    private final Text text;
    private final List<String> strings;

    Writer(Text text, List<String> strings) {
        this.text = text;
        this.strings = strings;
    }

    @Override
    public void run() {
        for (String string : strings) {
            text.addText(string);
        }
    }
}
