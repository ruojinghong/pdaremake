package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TallyBean(var shelf_code:String = "",
                        var goods_code:String="",
                     //1是店内码 2是条形码 3是epc
                      var goods_code_type:String="",
                     var num:String = "",):Parcelable
