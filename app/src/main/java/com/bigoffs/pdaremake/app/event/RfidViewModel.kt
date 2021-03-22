package com.bigoffs.pdaremake.app.event

import com.bigoffs.pdaremake.app.service.IScanService
import com.bigoffs.pdaremake.app.service.ScanServiceControl
import com.bigoffs.pdaremake.app.util.CacheUtil
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import java.util.*
import kotlin.collections.HashMap

class RfidViewModel : BaseViewModel() {


    var num = 0

    //已采集的数据
    var map: HashMap<String, String?> = HashMap()

    //继初始化已采集数据之后，新增的数据都会加入到这个集合中
    var out: LinkedList<String?> = LinkedList()
    companion object{
        private  var currentFacility = Facility.YBX
    }



    private val mService: IScanService = ScanServiceControl.getScanService()


    fun initOut(datas: List<String>) {
        out.addAll(datas)
    }

    fun initMap(datas: List<String>) {
        for (data in datas) {
            map.put(data, null)
            num++
        }
    }

    fun initData() {
        num = 0
        map.clear()
        out.clear()
    }

    // 1返回@+信号强度
    fun setReadDataModel(model: Int) {
        mService.setReadDataMode(model)
    }

    //设置模式，0 = 是混合模式 ， 1 = 只读标签 ， 2 = 只扫描条码
    fun setMode(mode: Int) {
        mService.setMode(mode)
    }

    //初始化已采集的数据
    fun initData(datas: List<String>) {
        for (data in datas) {
            map[data] = null
            num++
        }
    }

    //清理部分已采集的数据
    fun clearPartOfData(datas: List<String>) {
        for (data in datas) {
            map.remove(data)
            out.remove(data)
            num--
        }
    }

    //开始读标签
    fun startReadRfid() {
        mService.startInventory()
    }

    //停止读标签
    fun stopReadRfid() {
        mService.stopInventory()
    }

    //开始扫条码
    fun startScanBarcode() {
        mService.scanBarcode()
    }

    //停止扫条码
    fun setCurrentSetting(setting: Setting) {
        mService.setCurrentSetting(Setting.getSettingMap(setting))
    }

    //设置回调,没有通过内存排重的机制
    fun setListener(listener: OnFinishListener) {
        mService.setListener(listener)
    }

    //多增加一个监听
    fun setAnotherListener(listener: OnFinishListener?) {
        mService.setAnotherListener(listener)
    }

    fun removeAnotherListener() {
        mService.removeAnotherListener()
    }

    //清除回调
    fun removeListener() {
        mService.removeListener()
    }

    //设置回调，通过内存排重
    fun setListenerProtectModel(listener: OnFinishListener) {
        mService.setListener(object : OnFinishListener {
            override fun onFinish(data: String) {
                map.put(data, "")
                if (num < map.size) {
                    num++
                    out.add(data)
                    listener.onFinish(data)
                }
            }


        })
    }

    //在有携带能量值的标签集合中，找到能量最强的，也是离手持机最近的epc
    fun searchNearestEpc(tags: List<String>): String? {
        var treeMap: MutableMap<Int, String> = TreeMap()
        var nums: ArrayList<Int> = ArrayList()
        try {
            for (data in tags) {
                val split = data.split("@".toRegex()).toTypedArray()
                val tag = split[0]
                var rssi: Int
                rssi = if (split.size > 1) {
                    split[1].toInt()
                } else continue
                treeMap[rssi] = tag
                nums.add(rssi)
            }


            Collections.sort(nums, object : Comparator<Int> {


                override fun compare(o1: Int, o2: Int): Int {
                    return o2 - o1
                }
            })
            return treeMap[nums[0]]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    enum class Setting {
        binding, bindingMore, stockRead;

        companion object {
            fun getSettingMap(setting: Setting): Map<String, Any> {
                val map: MutableMap<String, Any> =
                    HashMap()
                when (setting) {
                    binding -> when (currentFacility) {
                        Facility.YBX -> map["power"] =
                            CacheUtil.getPower()
                        Facility.CW -> map["power"] = 10
                    }
                    bindingMore -> when (currentFacility) {
                        Facility.YBX -> map["power"] =
                            CacheUtil.getPower()
                        Facility.CW -> map["power"] = 30
                    }
                    stockRead -> when (currentFacility) {
                        Facility.YBX -> map["power"] =
                            CacheUtil.getPower()
                        Facility.CW -> map["power"] = 30
                    }
                    else -> {
                    }
                }
                return map
            }
        }
    }

    internal enum class Facility {
        YBX, CW
    }

     interface OnFinishListener{
            fun onFinish(data:String)
    }


}

