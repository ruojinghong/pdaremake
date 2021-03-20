package com.bigoffs.pdaremake.app.service.YBX;

/**
 * 读取模式
 *
 */

public enum ScanMode {
    FIND("找货模式", "0", "马上返回被读到的标签(session0)"),
    TAKE_STOCK("盘点模式", "1", "标签被读到以后，会暂时休眠0.5-2.0s(session1)");

    ScanMode(String name, String value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    private String name;
    private String value;
    private String description;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
