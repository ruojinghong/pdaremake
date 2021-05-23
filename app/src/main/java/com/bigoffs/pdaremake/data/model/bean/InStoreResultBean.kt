package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InStoreResultBean(
    val id: Int = 0,
    val serial_no: String = "",
    val w_id: Int = 0,
    val type: Int = 0,
    val finish_at: String = "",
    val status: Int = 0,
    val total_num: Int = 0,
    val final_num: Int = 0
) : Parcelable
