package com.rain.fabricdemo.handler;

import com.rain.fabricdemo.dto.DataItem;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class StreamDataHandler implements DataHandler {
    //记录所有的数据
    protected static final ConcurrentHashMap<String, List<DataItem>> dataPool = new ConcurrentHashMap<>();

    //数据读入之前
    abstract void beforeHandle ();

    //数据读入之后
    abstract void afterHandle (DataItem in);

    void handleData (DataItem in) {
        beforeHandle();
       /* List<DataItem> dataItems = dataPool.get(dataType);
        if (dataItems == null) {
            dataItems = new ArrayList<>();
        }
        dataItems.add(in);
        dataPool.put(dataType, dataItems);*/

        handle(in);

        afterHandle(in);
    }

}
