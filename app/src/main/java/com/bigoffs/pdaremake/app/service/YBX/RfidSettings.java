package com.bigoffs.pdaremake.app.service.YBX;

/**
 * RFID设置
 *
 */

public class RfidSettings {
    /**
     * 频段(0\1\2\3\4)
     */
    private int     frequencyBand = 1;
    /**
     * 最小频率
     */
    private int     minFrequent = 0;
    /**
     * 最大频率
     */
    private int     maxFrequent = 19;
    /**
     * 功率
     */
    private int     power = 30;
    /**
     * 启用声音
     */
    private boolean enableSound = false;

    /**
     * 读取间隔(ms)
     */
    private int interval = 100;


    public int getFrequencyBand() {
        return frequencyBand;
    }

    public void setFrequencyBand(int frequencyBand) {
        this.frequencyBand = frequencyBand;
    }

    public int getMinFrequent() {
        return minFrequent;
    }

    public void setMinFrequent(int minFrequent) {
        this.minFrequent = minFrequent;
    }

    public int getMaxFrequent() {
        return maxFrequent;
    }

    public void setMaxFrequent(int maxFrequent) {
        this.maxFrequent = maxFrequent;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public boolean getEnableSound() {
        return enableSound;
    }

    public void setEnableSound(boolean enableSound) {
        this.enableSound = enableSound;
    }
}
