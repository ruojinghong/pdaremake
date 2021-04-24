package com.bigoffs.pdaremake.data.model.bean


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * 找同款bean类
 */
@Parcelize
data class FindSame(
    val brand_id: Int = 0,
    val brand_name: String = "",
    val category_id: Int = 0,
    val category_name: String = "",
    val image: String = "",
    val property_list: List<Property> = mutableListOf(),
    val sale_price: Int = 0,
    val spec_list: List<Spec> = mutableListOf(),
    val spu_id: Int = 0,
    val spu_name: String = "",
    val spu_no: String = "",
    val stock_map: List<StockMap> = mutableListOf(),
    val stock_num: Int = 0
):Parcelable{
    @Parcelize
data class Spec(
    val spec_key: String = "",
    val spec_value: String = ""
):Parcelable
    @Parcelize
data class StockMap(
    val barcode: String = "",
    val shelf_code: String = "",
    val spec_list: List<SpecX>,
    val stock_num: Int = 0
):Parcelable
    @Parcelize
data class SpecX(
    val spec_key: String = "",
    val spec_value: String = ""
):Parcelable
}