package com.rain.fabricdemo.handler;

import com.rain.fabricdemo.dto.DataItem;

public interface DataHandler {
    /**
     * 处理数据的接口
     *
     * @param in       接收到的一条实时数据
     */
    void handle(DataItem in);

}
