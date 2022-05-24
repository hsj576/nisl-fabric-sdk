package com.rain.fabricdemo.test;

import com.rain.fabricdemo.handler.FirstSampleHandler;
import com.rain.fabricdemo.ledger.ConsistentHashing;
import com.rain.fabricdemo.stream.StreamDataSubscribe;
import com.rain.fabricdemo.handler.DataHandler;

import static com.rain.fabricdemo.ledger.QueryLedger.TRANSACT_SAVINGS_FAIL_NUM;
import static com.rain.fabricdemo.ledger.QueryLedger.TRANSACT_SAVINGS_SUCCESS_NUM;
import static com.rain.fabricdemo.ledger.QueryLedger.DEPOSIT_CHECKING_FAIL_NUM;
import static com.rain.fabricdemo.ledger.QueryLedger.DEPOSIT_CHECKING_SUCCESS_NUM;
import static com.rain.fabricdemo.ledger.QueryLedger.SEND_PAYMENT_FAIL_NUM;
import static com.rain.fabricdemo.ledger.QueryLedger.SEND_PAYMENT_SUCCESS_NUM;
import static com.rain.fabricdemo.ledger.QueryLedger.WRITE_CHECK_FAIL_NUM;
import static com.rain.fabricdemo.ledger.QueryLedger.WRITE_CHECK_SUCCESS_NUM;
import static com.rain.fabricdemo.ledger.QueryLedger.BALANCE_FAIL_NUM;
import static com.rain.fabricdemo.ledger.QueryLedger.BALANCE_SUCCESS_NUM;
import static com.rain.fabricdemo.ledger.QueryLedger.AMALGAMATE_FAIL_NUM;
import static com.rain.fabricdemo.ledger.QueryLedger.AMALGAMATE_SUCCESS_NUM;

public class App {
    public static ConsistentHashing ch;
    public static void main(String[] args) {
        ch=new ConsistentHashing();
        DataHandler handler = new FirstSampleHandler();
        Object lock = new Object();
        StreamDataSubscribe streamDataSubscribe = new StreamDataSubscribe(handler);
        long beginTime, endTime;

        beginTime = System.currentTimeMillis();
        streamDataSubscribe.start();
        streamDataSubscribe.stop();
        endTime = System.currentTimeMillis();
        System.out.printf("time: %d ms\n", endTime - beginTime);
        int SUCCESS_NUM=0;
        int FAIL_NUM=0;
        SUCCESS_NUM = TRANSACT_SAVINGS_SUCCESS_NUM + DEPOSIT_CHECKING_SUCCESS_NUM + SEND_PAYMENT_SUCCESS_NUM + WRITE_CHECK_SUCCESS_NUM + BALANCE_SUCCESS_NUM + AMALGAMATE_SUCCESS_NUM;
        FAIL_NUM = TRANSACT_SAVINGS_FAIL_NUM + DEPOSIT_CHECKING_FAIL_NUM + SEND_PAYMENT_FAIL_NUM + WRITE_CHECK_FAIL_NUM + BALANCE_FAIL_NUM + AMALGAMATE_FAIL_NUM;
        System.out.println("success transection:"+SUCCESS_NUM+" fail transection:"+FAIL_NUM);
        System.out.println("success TRANSACT_SAVINGS:"+TRANSACT_SAVINGS_SUCCESS_NUM+" fail TRANSACT_SAVINGS:"+TRANSACT_SAVINGS_FAIL_NUM);
        System.out.println("success DEPOSIT_CHECKING:"+DEPOSIT_CHECKING_SUCCESS_NUM+" fail DEPOSIT_CHECKING:"+DEPOSIT_CHECKING_FAIL_NUM);
        System.out.println("success SEND_PAYMENT:"+SEND_PAYMENT_SUCCESS_NUM+" fail SEND_PAYMENT:"+SEND_PAYMENT_FAIL_NUM);
        System.out.println("success WRITE_CHECK:"+WRITE_CHECK_SUCCESS_NUM+" fail WRITE_CHECK:"+WRITE_CHECK_FAIL_NUM);
        System.out.println("success BALANCE:"+BALANCE_SUCCESS_NUM+" fail BALANCE:"+BALANCE_FAIL_NUM);
        System.out.println("success AMALGAMATE:"+AMALGAMATE_SUCCESS_NUM+" fail AMALGAMATE:"+AMALGAMATE_FAIL_NUM);
    }
}