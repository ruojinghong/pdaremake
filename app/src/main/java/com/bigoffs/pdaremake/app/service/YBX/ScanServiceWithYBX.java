package com.bigoffs.pdaremake.app.service.YBX;

import android.content.Context;
import android.device.ScanManager;
import android.os.Handler;
import android.os.Message;


import com.bigoffs.UHF.scanlable.UfhData;
import com.bigoffs.pdaremake.app.service.CW.ScanServiceWithCW;
import com.bigoffs.pdaremake.app.service.IScanService;
import com.bigoffs.pdaremake.app.service.OnFinishListener;
import com.bigoffs.pdaremake.app.util.SPUtils;
import com.bigoffs.pdaremake.app.util.SoundUtils;
import com.orhanobut.logger.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */

public class ScanServiceWithYBX implements IScanService {
    private int selectTime = 1;
    private int selectedEd = 0;
    private Timer timer;                                //扫描计时器

    //默认参数
    private int power = 30;
    private int readDataMode = 0;

    private static RfidSettings currentSettings = null;
    private static OnFinishListener mListener;
    private static OnFinishListener listener;
    private static ReadedListener readedListener;
    private Handler mHandler;

    private static final int MSG_SHOW_PROPERTIES = 0;
    private static final int MSG_UPDATE_LISTVIEW = 1;
    private boolean isCanceled = true;
    private boolean scanflag = false;
    //默认只读取标签，扫描条形码由PDA设置获取
//    private int currentMode = 0;
    private int currentMode = 1;
    private int readMode = 1;
    private int scanMode = 2;

    private static ScanServiceWithYBX instance = null;
    public ScanManager mScanManager;

    public static ScanServiceWithYBX getInstance() {
        if (instance == null) {

            synchronized (ScanServiceWithCW.class) {
                if (instance == null) {
                    instance = new ScanServiceWithYBX();
                }
            }
        }
        return instance;
    }

    @Override
    public void init(Context context) {
        SoundUtils.initSoundPool(context);
        power = (int) SPUtils.get(context, "power", 30);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SHOW_PROPERTIES:
                        currentSettings.setFrequencyBand(UfhData.UhfGetData.getband()[0]);
                        currentSettings.setMinFrequent(UfhData.UhfGetData.getUhfMinFre()[0]);
                        currentSettings.setMaxFrequent(UfhData.UhfGetData.getUhfMaxFre()[0]);
                        currentSettings.setPower(UfhData.UhfGetData.getUhfdBm()[0]);

                        logRfidSettings();

                        break;
                    case MSG_UPDATE_LISTVIEW:
                        if (isCanceled) return;
                        Map<String, Long> scanResult6c = UfhData.scanResult6c;
                        if (scanResult6c.isEmpty()) return;
                        RfidContext rfidContext = convert(scanResult6c);
                        readedListener.onReaded(rfidContext);
                        scanResult6c.clear();
                        break;
                }
            }
        };

        readedListener = new ReadedListener() {
            @Override
            public void onReaded(RfidContext context) {

                for (Rfid rfid : context.getRfids()) {
                    String epc = null;
                    StringBuilder sb = new StringBuilder();
                    if (rfid.getTagValue().toCharArray().length > 32)
                        continue;
                    sb.append(rfid.getTagValue().toUpperCase());
                    switch (readDataMode) {
                        case 0:
                            epc = sb.toString();
                            Logger.d(epc);
                            break;
                        case 1:
                            sb.append("@");
                            sb.append(ScanRSSI(rfid.getRssi().toString()));
                            epc = sb.toString();
                            break;
                    }

                    if (epc == null) return;

                    if (currentMode == scanMode) return;

                    mListener.onFinish(epc);
                    if (listener != null) {
                        listener.onFinish(epc);
                    }

                }
            }
        };
    }

    @Override
    public void startInventory() {
        if (isOpened() == false) Logger.d("设备未开！");
        if (timer != null) {
            return;
        }
        switch (readDataMode) {
            case 0:
                selectedEd = Integer.parseInt(ScanMode.TAKE_STOCK.getValue());
                break;
            case 1:
                selectedEd = Integer.parseInt(ScanMode.FIND.getValue());
        }

        UfhData.scanResult6c.clear();

        Logger.d("读写器正在启动读取...");

        scanflag = false;
        isCanceled = false;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (scanflag) return;
                scanflag = true;
                UfhData.read6c(selectedEd);
                try {
                    Thread.sleep(selectTime * 20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.removeMessages(MSG_UPDATE_LISTVIEW);
                mHandler.sendEmptyMessage(MSG_UPDATE_LISTVIEW);
                scanflag = false;
            }
        }, 0, currentSettings.getInterval());

    }

    @Override
    public void stopInventory() {
        Logger.d("读写器正在停止读取...");
        isCanceled = true;
        if (timer != null) {
            timer.cancel();
            timer = null;
            Logger.d("读写器已停止读取");
        }
    }

    @Override
    public void setReadDataMode(int readDataMode) {
        this.readDataMode = readDataMode;
    }

    @Override
    public void setListener(OnFinishListener listener) {
        mListener = listener;
    }

    @Override
    public void setAnotherListener(OnFinishListener listener) {
        this.listener = listener;
    }

    @Override
    public void removeListener() {
        mListener = null;
    }

    @Override
    public void removeAnotherListener() {
        listener = null;
    }

    @Override
    public boolean openReader() {
        if (isOpened()) {
            return true;
        }
        //重新打开读写器，需要重置状态
        currentSettings = new RfidSettings();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final int ttySpeed = 57600;
                final byte addr = (byte) 0xff;
                int result = UfhData.UhfGetData.OpenUhf(ttySpeed, addr, 4, 0, null);
                if (result == 0) {
                    Logger.d("读写器启动成功");
                    Logger.d("正在加载读写器设置");
                    UfhData.UhfGetData.GetUhfInfo();
                    mHandler.removeMessages(MSG_SHOW_PROPERTIES);
                    mHandler.sendEmptyMessage(MSG_SHOW_PROPERTIES);
                }
            }
        });
        thread.start();
        return true;
    }

    @Override
    public boolean closeReader() {
        if (this.isOpened() == false) {
            Logger.d("读写器状态为已关闭，不需要执行关闭操作");
            return true;
        }
        Logger.d("读写器正在关闭...");
        UfhData.UhfGetData.CloseUhf();
        Logger.d("读写器已关闭");
        return !isOpened();
    }

    @Override
    public boolean openBarcodeReader(Context context) {
        mScanManager = new ScanManager();
        mScanManager.openScanner();
        mScanManager.switchOutputMode(0);

        BarcodeReceiver.setListener(new OnFinishListener() {
            @Override
            public void onFinish(String data) {
                if (currentMode == readMode) return;
                if (mListener != null) {
                    mListener.onFinish(data);

                }
                if (listener != null) {
                    listener.onFinish(data);

                }

            }
        });

        return true;
    }

    @Override
    public boolean closeBarcodeReader() {
        mScanManager.stopDecode();
        return true;
    }

    @Override
    public void scanBarcode() {

        mScanManager.stopDecode();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mScanManager.startDecode();
    }

    @Override
    public void stopScanBarcode() {
        mScanManager.stopDecode();
    }

    private RfidContext convert(Map<String, Long> data) {
        RfidContext rfidContext = new RfidContext();

        Set<Map.Entry<String, Long>> entrySet = data.entrySet();
        Iterator<Map.Entry<String, Long>> iterator = entrySet.iterator();
        try {
            while (iterator.hasNext()) {
                Map.Entry<String, Long> next = iterator.next();
                rfidContext.getRfids().add(new Rfid(next.getKey(), next.getValue()));
            }
        } catch (Exception e) {
            Logger.e(e.toString());
        }

        return rfidContext;
    }

    @Override
    public void setCurrentSetting(Map<String, Object> map) {
//        currentSettings.setPower(((int) map.get("power")));
        currentSettings.setPower(power);
        set(currentSettings);
    }

    public void set(final RfidSettings settings) {
        if (isOpened() == false) {
            Logger.d("读写器已关闭");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                currentSettings.setFrequencyBand(settings.getFrequencyBand());
                currentSettings.setMinFrequent(settings.getMinFrequent());
                currentSettings.setMaxFrequent(settings.getMaxFrequent());
                currentSettings.setPower(settings.getPower());
                currentSettings.setInterval(settings.getInterval());
                currentSettings.setEnableSound(settings.getEnableSound());

                int band = convertFrequencyBand(currentSettings.getFrequencyBand());
                int MinFre = ((band & 3) << 6) | (currentSettings.getMinFrequent() & 0x3F);                 //频率和最小频点(0-31(int型))
                int MaxFre = ((band & 0x0c) << 4) | (currentSettings.getMaxFrequent() & 0x3F);              //频率和最大频点(0-31(int型))
                int Power = currentSettings.getPower();                                                    //扫描功率(0-30)

                UfhData.UhfGetData.SetUhfInfo((byte) MaxFre, (byte) MinFre, (byte) Power, (byte) 0);        //设置频段、频率、功率

                logRfidSettings();

            }
        }).start();
    }

    @Override
    public void setMode(int mode) {
        currentMode = mode;
    }

    private boolean isOpened() {
        return UfhData.isDeviceOpen();
    }

    private static void logRfidSettings() {
        Logger.d("读写器参数"
                .concat("，频段：" + currentSettings.getFrequencyBand())
                .concat("，最小频率：" + currentSettings.getMinFrequent())
                .concat("，最大频率：" + currentSettings.getMaxFrequent())
                .concat("，功率：" + currentSettings.getPower())
                .concat("，读取频率：" + currentSettings.getInterval() + " ms")
        );
    }

    public int getPower() {
        return power;
    }

    public void setPower(int p) {
        power = p;
    }
    private int ScanRSSI(String rssi) {
        int irssi = Integer.parseInt(rssi);
        int scanRSSI = 0;
        if (irssi > 100) {
            scanRSSI = 5;
        } else if (irssi >= 80 && irssi < 100) {
            scanRSSI = 4;
        } else if (irssi >= 70 && irssi < 80) {
            scanRSSI = 3;
        } else if (irssi >= 60 && irssi < 70) {
            scanRSSI = 2;
        } else if (irssi >= 50 && irssi < 60) {
            scanRSSI = 1;
        } else if (irssi >= 40) {
            scanRSSI = 0;
        }
        return scanRSSI;
    }

    /**
     * 频段设置转换
     *
     * @param frequencyBand
     * @return
     */
    private int convertFrequencyBand(int frequencyBand) {
        int result = 0;
        switch (frequencyBand) {
            case 0:
                result = 1;
                break;
            case 1:
                result = 2;
                break;
            case 2:
                result = 3;
                break;
            case 3:
                result = 4;
                break;
            case 4:
                result = 8;
                break;
        }
        return result;
    }
}
