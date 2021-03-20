package com.bigoffs.pdaremake.app.service;

import android.content.Context;

import com.bigoffs.rfid.listener.OnFinishListener;

import java.util.Map;

public interface IScanService {
    void init(Context context);

    void startInventory();

    void stopInventory();

    boolean openReader();

    boolean closeReader();

    boolean openBarcodeReader(Context context);

    boolean closeBarcodeReader();

    void scanBarcode();

    void stopScanBarcode();

    void setCurrentSetting(Map<String, Object> map);

    void setReadDataMode(int readDataMode);

    void setListener(OnFinishListener listener);

    void setAnotherListener(OnFinishListener listener);

    void removeListener();
    void removeAnotherListener();

    void setMode(int mode);
}
