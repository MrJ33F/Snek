package project.android.androidprojectsnek.utils;

//Clasa simpla care se ocupa cu rularea task-urilor periodic

import java.util.concurrent.atomic.AtomicBoolean;

public class Timer {

    private final long interval;
    private final Runnable task;
    private AtomicBoolean running;

    private final Object lock = new Object();

    public Timer(Runnable task, long intervalMs) {
        this.task = task;
        this.interval = intervalMs;
        running = new AtomicBoolean(false);
    }

    public void start() {
        running.set(true);

        new Thread(() -> {
            while (running.get()) {
                synchronized (lock) {
                    try {
                        lock.wait(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    task.run();
                }
            }
        }).start();

    }

    public void stop() {
        running.set(false);
    }

}
