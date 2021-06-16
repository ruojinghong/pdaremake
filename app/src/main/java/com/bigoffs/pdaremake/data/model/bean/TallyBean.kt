package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TallyBean(var shelf_code:String = "",
                    var nums:Int = 0,
                    var code:String=""):Parcelable
