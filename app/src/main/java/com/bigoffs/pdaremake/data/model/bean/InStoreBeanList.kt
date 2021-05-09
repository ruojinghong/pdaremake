package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InStoreBeanList(
    val list:MutableList<InStoreBean> = mutableListOf(),
    val count:Int = 0,
    val limit:Int= 0
):Parcelable