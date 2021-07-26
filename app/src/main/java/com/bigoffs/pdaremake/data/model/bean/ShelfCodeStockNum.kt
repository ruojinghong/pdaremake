package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShelfCodeStockNum(var shelf_code :String= "",var stock_num:String = "",var spec_list:ArrayList<SpecList>):Parcelable
@Parcelize
data class SpecList(
    val spec_key: String,
    val spec_value: String
): Parcelable
