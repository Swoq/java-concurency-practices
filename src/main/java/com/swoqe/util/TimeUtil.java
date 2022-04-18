package com.swoqe.util;

public class TimeUtil {
    public static long recordTime(Runnable runnable) {
        long currTime = System.nanoTime();
        runnable.run();
        return System.nanoTime() - currTime;
    }
}
