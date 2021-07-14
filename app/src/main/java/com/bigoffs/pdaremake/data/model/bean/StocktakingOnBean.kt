package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StocktakingOnBean(var shelf_code:String = "",var goods_code:String = "",
                             var num:String = "",var goods_code_type:String="2",
                             var old_shelf_code:String = "",var old_num:String = "",):Parcelable
