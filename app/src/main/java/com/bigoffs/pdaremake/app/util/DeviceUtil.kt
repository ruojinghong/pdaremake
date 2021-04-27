package com.bigoffs.pdaremake.app.util

import com.bigoffs.pdaremake.app.ext.DeviceType
import com.blankj.utilcode.util.DeviceUtils

/**
 * 判断是什么设备
 */
object DeviceUtil {
    //是带有rfid的设备
    fun isRfidDevice():Boolean{

        return  DeviceUtils.getModel()== DeviceType.RFID.model
    }

    //普通的pda不带rfid
    fun isPdaDevice():Boolean{

        return  DeviceUtils.getModel()== DeviceType.PDA.model
    }
}