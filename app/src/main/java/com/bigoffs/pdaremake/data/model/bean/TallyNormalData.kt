package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TallyNormalData(
    val data: List<Data>,
    val total: Int
):Parcelable
@Parcelize
data class Data(
    var shelf:String,
    val code: String,
    val num: Int
):Parcelable