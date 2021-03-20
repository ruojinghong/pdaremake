package com.bigoffs.pdaremake.app.service.YBX;

import java.util.ArrayList;
import java.util.List;


public class RfidContext {
    /**
     * RFID集合
     */
    private List<Rfid> rfids = new ArrayList<>();

    public List<Rfid> getRfids() {
        return rfids;
    }

    public void setRfids(List<Rfid> rfids) {
        this.rfids = rfids;
    }
}
