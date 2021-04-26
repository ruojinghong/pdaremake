package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BarcodeFind(
    val barcode: String = "",
    val epc_codes: List<String> = mutableListOf(),
    val shelf_code: String = "",
    val sku_id: Int = 0
):Parcelable