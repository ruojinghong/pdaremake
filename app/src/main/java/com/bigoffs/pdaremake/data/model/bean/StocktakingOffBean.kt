package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StocktakingOffBean(var code:String = "",var num:Int = 0):Parcelable
