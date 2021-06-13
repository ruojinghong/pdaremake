package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import com.bigoffs.pdaremake.app.util.CacheUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewInStoreNormalBarcodeAndUniqueBean(
    var shelf_code:String = "",
    var barcode:String = "",
    var unique_code:String = "",
    var num:String = "",
    var admin_id:String = CacheUtil.getUser()?.userInfo?.uid+"",
    var maxNum : Int = 999):Parcelable
