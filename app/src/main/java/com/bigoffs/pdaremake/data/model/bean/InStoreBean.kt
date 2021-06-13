package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InStoreBean(
    val admin_id: Int = 0,
    val admin_name: String = "",
    var arrival_no: String? = "",
    val batch_type: Int = 0,
    val batch_type_name: String = "",
    val brand_names: String = "",
    val created_at: String = "",
    val final_num: Int = 0,
    val id: Int = 0,
    val in_stock_no: String? = "",
    val order_no: String = "",
    val serial_no: String = "",
    val in_ware_name: String = "",
    val out_ware_name: String = "",
    val status: Int = 0,
    val status_name: String = "",
    val sup_id: Int = 0,
    val sup_name: String = "",
    val total_num: Int = 0,
    val type: Int = 0,
    val updated_at: String = "",
    val w_id: Int = 0,
    val ware_name: String = ""
):Parcelable