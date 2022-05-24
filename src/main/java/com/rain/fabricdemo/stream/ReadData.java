package com.rain.fabricdemo.stream;

import com.rain.fabricdemo.dto.DataItem;
import com.rain.fabricdemo.handler.DataHandler;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import java.util.Random;

public class ReadData extends TimerTask {
    private int maxDataSize = 1;
    private static final Object lock = new Object();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(300);//线程数量
    private Timer timer;
    private static int generateOnce = 10000;
    private static final Long period = 100L; // 单位是ms    每隔一段时间产生一定量的数据
    private final Set<DataItem> DataPool = new HashSet<>();

    public ReadData() {
        Timer timer = new Timer();
        // 1模拟生产者和消费者
        //timer.scheduleAtFixedRate(this, 0, period);
        // 2一次性生产出来数据
        timer.schedule(this, 3);
        this.timer = timer;
    }

    public long read(final DataHandler handler) {
        int hasReadItemCount = 0;
        boolean isLimit = maxDataSize > 0;

        List<DataItem> needToRemoveList = new ArrayList<>();
        while (true) {
            synchronized (lock) {
                if (DataPool.size() <= 0) {
                    continue;
                }
                needToRemoveList.clear();
                for (final DataItem dataItem : DataPool) {
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                handler.handle(dataItem);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    needToRemoveList.add(dataItem);

                    if (isLimit == true) {
                        hasReadItemCount++;
                    }
                }
                DataPool.removeAll(needToRemoveList);
            }
            if (isLimit == true && hasReadItemCount >= maxDataSize) {
                break;
            }
        }
        executorService.shutdown();

        try {
            while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                System.out.println("1 second passed...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("read over");
        return System.currentTimeMillis();
    }

    public void preProduceSomeRequest() {
        List<DataItem> dataItems = new ArrayList<>();
        Random random = new Random(1);
        for (int i = 0; i < generateOnce; i++) {
            // 随机生成查询请求
            int hot = random.nextInt(10);
            int my_bound = 0;
            if (hot > 5) {
                my_bound = 1000;
            } else {
                my_bound = 250;
            }

            DataItem dataItem = new DataItem();
            int account1 = random.nextInt(my_bound);
            int account2 = random.nextInt(my_bound);
            if (account1 == account2) {
                account2 = (account2 + 1)%my_bound;
            }
            dataItem.from = String.valueOf(account1);
            dataItem.to = String.valueOf(account2);

            int op = random.nextInt(7);
            switch (op) {
                case 1:
                    dataItem.operation = "AMALGAMATE";
                    break;
                case 2:
                    dataItem.operation = "BALANCE";
                    break;
                case 3:
                    dataItem.operation = "DEPOSIT_CHECKING";
                    break;
                case 4:
                    dataItem.operation = "TRANSACT_SAVINGS";
                    break;
                case 5:
                    dataItem.operation = "WRITE_CHECK";
                    break;
                default:
                    dataItem.operation = "SEND_PAYMENT";
                    break;
            }
            dataItems.add(dataItem);
        }

        synchronized (lock) {
            DataPool.addAll(dataItems);
        }
    }

    // 模拟生产者和消费者模型，每隔一段时间生产一些数据
    @Override
    public void run() {
        List<DataItem> dataItems = new ArrayList<>();
        Random random = new Random(1);
        for (int i = 0; i < generateOnce; i++) {
            // 随机生成查询请求
            int hot = random.nextInt(10);
            int my_bound = 0;
            if (hot > 5) {
                my_bound = 1000;
            } else {
                my_bound = 250;
            }

            DataItem dataItem = new DataItem();
            int account1 = random.nextInt(my_bound);
            int account2 = random.nextInt(my_bound);
            if (account1 == account2) {
                account2 = (account2 + 1)%my_bound;
            }
            dataItem.from = String.valueOf(account1);
            dataItem.to = String.valueOf(account2);

            int op = random.nextInt(7);
            switch (op) {
                case 1:
                    dataItem.operation = "AMALGAMATE";
                    break;
                case 2:
                    dataItem.operation = "BALANCE";
                    break;
                case 3:
                    dataItem.operation = "DEPOSIT_CHECKING";
                    break;
                case 4:
                    dataItem.operation = "TRANSACT_SAVINGS";
                    break;
                case 5:
                    dataItem.operation = "WRITE_CHECK";
                    break;
                default:
                    dataItem.operation = "SEND_PAYMENT";
                    break;
            }
            dataItems.add(dataItem);
        }

        synchronized (lock) {
            DataPool.addAll(dataItems);
        }
    }

    public void stop() {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }
}
