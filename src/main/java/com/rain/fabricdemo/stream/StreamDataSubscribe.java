package com.rain.fabricdemo.stream;

import com.rain.fabricdemo.handler.DataHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class StreamDataSubscribe {
    private DataHandler handler;
    private Set<ReadData> tasks;
    private static final Object lock = new Object();

    public StreamDataSubscribe(DataHandler handler) {
        super();
        this.handler = handler;
        this.tasks = new HashSet<>();
    }

    public void start() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReadData readData = new ReadData();
                readData.preProduceSomeRequest();
                readData.read(handler);
                synchronized (lock) {
                    tasks.add(readData);
                }

                try {
                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (this.tasks != null && tasks.size() > 0) {
            for (ReadData task: tasks) {
                task.stop();
            }
        }
    }
}
