package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class StocktakingBean(var total:String = "",var data : MutableList<StocktakingListBean>):Parcelable
