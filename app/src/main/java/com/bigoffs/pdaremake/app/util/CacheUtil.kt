package com.bigoffs.pdaremake.app.util

import android.text.TextUtils
import com.bigoffs.pdaremake.data.model.bean.House
import com.bigoffs.pdaremake.data.model.bean.User
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

object CacheUtil {


    /**
     * 获取保存的账户信息
     */
    fun getUser(): User? {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, User::class.java)
        }
    }

    /**
     * 设置账户信息
     */
    fun setUser(userResponse: User?) {
        val kv = MMKV.mmkvWithID("app")
        if (userResponse == null) {
            kv.encode("user", "")
            setIsLogin(false)
        } else {
            kv.encode("user", Gson().toJson(userResponse))
            setIsLogin(true)
        }

    }
    /**
     * 设置仓库ID
     */
    fun setHouse(house: House?) {
        val kv = MMKV.mmkvWithID("app")
        if (house == null) {
            kv.encode("house", "")
        } else {
            kv.encode("house", Gson().toJson(house))
        }

    }

    /**
     * 获取仓库ID
     */
    fun getHouse() : House? {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("house")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, House::class.java)
        }

    }


    /**
     * 设置无线射频信号强度
     */
    fun setPower(value: Int) {
        val kv = MMKV.mmkvWithID("app")
        if(0<value&&value>30){
            value == 30
        }
            kv.encode("power",value)


    }

    /**
     * 获取保存的账户信息
     */
    fun getPower(): Int {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeInt("power",30)

    }

    /**
     * 是否已经登录
     */
    fun isLogin(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("login", false)
    }

    /**
     * 设置是否已经登录
     */
    fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("login", isLogin)
    }

    /**
     * 是否是第一次登陆
     */
    fun isFirst(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("first", true)
    }
    /**
     * 是否是第一次登陆
     */
    fun setFirst(first:Boolean): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("first", first)
    }


     fun  getInt(name:String,defaultValue:Int): Int {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeInt(name,defaultValue)
    }

    fun setInt(name:String,value:Int):Boolean{
        val kv = MMKV.mmkvWithID("app")
        return kv.encode(name, value)
    }
}