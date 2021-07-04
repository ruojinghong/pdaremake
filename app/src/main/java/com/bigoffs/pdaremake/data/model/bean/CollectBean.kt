package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import com.bigoffs.pdaremake.app.util.CacheUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectBean(
    var code1:String = "",
    var code2:String = "",
    var code3:String = "",
    var code4:String = "",
    var code5:String = "",
    var numberId: String? = CacheUtil.getUser()?.userInfo?.uid
                       ):Parcelable
