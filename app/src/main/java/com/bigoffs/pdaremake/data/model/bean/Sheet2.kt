package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sheet2(var shelf:String = "",var barcode:String = "",var unique:String = "",var num:Int = 0):Parcelable
