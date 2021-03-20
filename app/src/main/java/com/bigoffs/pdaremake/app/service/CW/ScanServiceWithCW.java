package com.bigoffs.pdaremake.app.service.CW;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.bigoffs.rfid.listener.OnFinishListener;
import com.bigoffs.rfid.persistence.util.SPUtils;
import com.bigoffs.rfid.persistence.util.SoundUtils;
import com.bigoffs.rfid.service.IScanService;
import com.rscja.deviceapi.Barcode1D;
import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.deviceapi.exception.ConfigurationException;
import com.zebra.adc.decoder.Barcode2DWithSoft;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanServiceWithCW implements IScanService {
    private RFIDWithUHF mReader;


    private boolean loopFlag = false;
    private ExecutorService executorService;
    private Handler mHandler;
    private static OnFinishListener mListener;
    //单独实现一个监听
    private static OnFinishListener listener;
    //默认参数
    private int power = 30;
    private int workTime = 5;
    private int waitTime = 5;
    private int Q = 15;
    private int between = 4;
    private int inventoryFlag = 1;
    private int readDataMode = 0;
    private Context context;


    private static ScanServiceWithCW instance = null;
    private Barcode2DWithSoft barcode2D;
    private Barcode1D barcode1D;

    public static ScanServiceWithCW getInstance() {
        if (instance == null) {

            synchronized (ScanServiceWithCW.class) {
                if (instance == null) {
                    instance = new ScanServiceWithCW();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        executorService = Executors.newCachedThreadPool();
        SoundUtils.initSoundPool(context);
        power = (int) SPUtils.get(context, "power", 30);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        mListener.OnFinish(msg.obj.toString());
                        listener.OnFinish(msg.obj.toString());
                    case 1:
                        mListener.OnFinish(msg.obj.toString());
                        listener.OnFinish(msg.obj.toString());
                }
            }
        };
    }

    //开始扫描
    public void startInventory() {

        switch (inventoryFlag) {
            case 0:// 单步
            {
                String strUII = mReader.inventorySingleTag();
                if (!TextUtils.isEmpty(strUII)) {
                    String strEPC = mReader.convertUiiToEPC(strUII);
                } else {
                    Log.d("mReadLift", "识别失败");
                }

            }
            break;
            case 1:// 单标签循环
            {

                if (mReader.startInventoryTag((byte) 0, (byte) 0)) {
                    loopFlag = true;
                    switch (readDataMode) {
                        case 0:
                            executorService.execute(new ScanThread());
                        case 1:
                            executorService.execute(new ScanThreadWithRssi());
                    }
                } else {
                    mReader.stopInventory();
                }
            }

            break;
            case 2:// 防碰撞
            {
                if (mReader.startInventoryTag((byte) 1, Q)) {
                    loopFlag = true;
                    switch (readDataMode) {
                        case 0:
                            executorService.execute(new ScanThread());
                        case 1:
                            executorService.execute(new ScanThreadWithRssi());
                    }
                } else {
                    mReader.stopInventory();
                }
            }
            break;
        }
    }

    //停止扫描
    public void stopInventory() {

        if (loopFlag) {

            loopFlag = false;
            if (mReader.stopInventory()) {
                Log.d("mReadLift", "停止识别成功");
            } else {
                Log.d("mReadLift", "停止识别失败");
            }
        }
    }

    //扫描线程
    class ScanThread extends Thread {


        public ScanThread() {
        }

        public void run() {

            while (loopFlag) {
                String UII = mReader.readUidFromBuffer();
                if (UII != null) {
                    String epc = mReader.convertUiiToEPC(UII);

                    mHandler.sendMessage(mHandler.obtainMessage(0, epc));
                }

                try {
                    sleep(between);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    class ScanThreadWithRssi extends Thread {

        public void run() {

            String[] res = null;

            while (loopFlag) {

                res = mReader.readTagFromBuffer();

                if (res != null) {
                    Message msg = mHandler.obtainMessage();
                    msg.obj = mReader.convertUiiToEPC(res[1]) + "@" + res[2];
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }

                try {
                    sleep(between);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public boolean openReader() {
        boolean init = false;
        try {

            mReader = RFIDWithUHF.getInstance();
            init = mReader.init();

            boolean setPower = mReader.setPower(power);
            boolean setPwm = mReader.setPwm(workTime, waitTime);
            boolean setReadMode = mReader.setReadMode(RFIDWithUHF.SingleModeEnum.MORE);
            boolean setFrequencyMode = mReader.setFrequencyMode((byte) 0);
            boolean setFreHop = mReader.setFreHop((float) 924.375);
            Log.d("test", " init:" + init
                    + " setPwm:" + setPwm
                    + " setPower:" + setPower
                    + " setReadMode:" + setReadMode
                    + " setFrequencyMode:" + setFrequencyMode
                    + " setFreHop:" + setFreHop);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return init;
    }

    @Override
    public void setCurrentSetting(Map<String, Object> map) {

    }

    public boolean closeReader() {
        return mReader.free();
    }

    public void setListener(OnFinishListener listener) {
        mListener = listener;
    }

    public void setAnotherListener(OnFinishListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        mListener = null;
    }
    public void removeAnotherListener() {
        listener = null;
    }

    public void setReadDataMode(int readDataMode) {
        this.readDataMode = readDataMode;
    }

    public boolean openBarcodeReader(Context context) {
        boolean open = false;
        try {
            if ((int) SPUtils.get(context, "barcodeReaderMode", 2) == 1) {
                barcode1D = Barcode1D.getInstance();
                open = barcode1D.open();
            } else {
                barcode2D = Barcode2DWithSoft.getInstance();
                open = barcode2D.open(context);
                if (open) {
                    barcode2D.setParameter(324, 1);
                    barcode2D.setParameter(300, 0); // Snapshot Aiming
                    barcode2D.setParameter(361, 0); // Image Capture Illumination
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return open;
    }

    public boolean closeBarcodeReader() {
        boolean close = false;
        if ((int) SPUtils.get(context, "barcodeReaderMode", 2) == 1) {
            close = barcode1D.close();
        } else {
            close = barcode2D.close();
        }
        return close;
    }

    public void scanBarcode() {
        if ((int) SPUtils.get(context, "barcodeReaderMode", 2) == 1) {
            String scan = barcode1D.scan();
            if (scan != null) {
                SoundUtils.play(1);
                mListener.OnFinish(scan);
                listener.OnFinish(scan);
            }
        } else {
            barcode2D.scan();
            barcode2D.setScanCallback(new Barcode2DWithSoft.ScanCallback() {
                @Override
                public void onScanComplete(int i, int i1, byte[] bytes) {
                    if (bytes != null) {
                        SoundUtils.play(1);
                        String barcode = new String(bytes);
                        int loc = barcode.indexOf('\u0000');
                        String newStr = barcode.substring(0, loc);
                        if (newStr == null) return;
                        mListener.OnFinish(newStr);
                        listener.OnFinish(newStr);
                    }
                }
            });
        }
    }

    public void stopScanBarcode() {
        if ((int) SPUtils.get(context, "barcodeReaderMode", 2) == 1) {
            if (barcode1D.isPowerOn())
                barcode1D.stopScan();
        } else {
            if (barcode2D.isPowerOn())
                barcode2D.stopScan();
        }
    }

    @Override
    public void setMode(int mode) {

    }
}
