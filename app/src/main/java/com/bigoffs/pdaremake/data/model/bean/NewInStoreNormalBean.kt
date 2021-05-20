package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewInStoreNormalBean(var shelf:String = "", var barcode:String = "", var unique:String = ""):Parcelable
