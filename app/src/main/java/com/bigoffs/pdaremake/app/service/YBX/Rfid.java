package com.bigoffs.pdaremake.app.service.YBX;

/**
 * RFID读写器返回数据结果封装
 * Created by tanqimin on 2016/11/14.
 */

public class Rfid {
    /**
     * EPC或TID
     */
    private String tagValue;
    /**
     * 信号强度
     */
    private Long rssi;

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public Long getRssi() {
        return rssi;
    }

    public void setRssi(Long rssi) {
        this.rssi = rssi;
    }

    public Rfid(String tagValue, Long rssi) {
        this.tagValue = tagValue;
        this.rssi = rssi;
    }

    @Override
    public String toString() {
        return "Rfid{" +
                "tagValue='" + tagValue + '\'' +
                ", rssi=" + rssi +
                '}';
    }
}
