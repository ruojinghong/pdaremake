package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class TallyAbnormalData(
    val data: List<AbData>,
    val total: Int
):Parcelable

@Parcelize
data class AbData(
    val code: String,
    val exception: String,
    val goods_code_type: Int
):Parcelable