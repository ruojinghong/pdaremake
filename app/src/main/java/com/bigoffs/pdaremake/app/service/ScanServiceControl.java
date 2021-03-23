package com.bigoffs.pdaremake.app.service;


import com.bigoffs.pdaremake.app.service.CW.ScanServiceWithCW;
import com.bigoffs.pdaremake.app.service.YBX.ScanServiceWithYBX;

public class ScanServiceControl {

    public static IScanService getScanService() {
        IScanService scanService = null;
        switch (2) {
            case 1:
                scanService = ScanServiceWithCW.getInstance();
                break;
            case 2:
                scanService = ScanServiceWithYBX.getInstance();
                break;
        }
        return scanService;
    }

}
