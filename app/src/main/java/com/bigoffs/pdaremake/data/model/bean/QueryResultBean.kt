package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 包含了 店内码查询和条形码查询所有字段
 */
@Parcelize
data class QueryResultBean(
    val barcode: String,
    val unique_code: String,
    val brand_id: Int,
    val brand_name: String,
    val category_id: Int,
    val category_name: String,
    val shelf_code: String,
    val image: String,
    val property_list: List<Property>,
    val sale_price: Int,
    val sku_id: Int,
    val spec_list: List<Spec>,
    val spu_id: Int,
    val spu_name: String,
    val spu_no: String,
    val stock_map: List<StockMap>,
    val stock_num: Int
): Parcelable
@Parcelize
data class Property(
    val property_key: String,
    val property_value: String
): Parcelable
@Parcelize
data class Spec(
    val spec_key: String,
    val spec_value: String
): Parcelable
@Parcelize
data class StockMap(
    val shelf_code: String,
    val stock_num: Int
): Parcelable