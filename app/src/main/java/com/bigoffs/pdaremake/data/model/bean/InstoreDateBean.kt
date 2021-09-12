package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InstoreDateBean(
    val num: Int = 0,
    val production_at: String = "",
):Parcelable