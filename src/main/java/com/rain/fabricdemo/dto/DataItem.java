package com.rain.fabricdemo.dto;


public class DataItem implements Comparable<DataItem> {

    public String operation;
    public String from;
    public String to;
    public int transfer;

    @Override
    public int compareTo (DataItem o) {
        return 0;
    }

}
