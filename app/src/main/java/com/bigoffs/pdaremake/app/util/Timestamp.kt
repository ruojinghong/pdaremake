package com.bigoffs.pdaremake.app.util

import java.text.ParsePosition
import java.text.SimpleDateFormat

/**
 *User:Kirito
 *Time:2021/9/8  0:31
 *Desc:
 */
class Timestamp {

    /**
     * Timestamp to String
     * @param Timestamp
     * @return String
     */
    fun transToString(time:Long):String{
        return SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(time)
    }

    /**
     * String to Timestamp
     * @param String
     * @return Timestamp
     */

    fun transToTimeStamp(date:String):Long{
        return SimpleDateFormat("yyyy-MM-dd HH-mm-ss").parse(date, ParsePosition(0)).time
    }
}