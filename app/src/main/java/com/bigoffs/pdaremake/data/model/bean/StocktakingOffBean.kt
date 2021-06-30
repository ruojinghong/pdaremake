package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StocktakingOffBean(var goods_code:String = "",var num:String = "",var shelf_code:String = "",var goods_code_type:String="2"):Parcelable
