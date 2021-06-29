package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CheckEpcCodeBean(
    val abnormal: Abnormal,
    val normal: Normal
):Parcelable
@Parcelize
data class Abnormal(
    val list: List<String>,
    val total: Int
):Parcelable
@Parcelize
data class Normal(
    val list: List<String>,
    val total: Int
):Parcelable