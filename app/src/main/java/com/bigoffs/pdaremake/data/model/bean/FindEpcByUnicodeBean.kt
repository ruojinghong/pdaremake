package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FindEpcByUnicodeBean(
    val epc_code: String = "",
    val shelf_code: String = "",
    val sku_id: Int = 0,
    val unique_code: String = ""
):Parcelable