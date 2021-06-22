package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StocktakingListBean(var id :Int = 0,
                                var name :String = "",
                                var book_stock :String = "",
                                var real_stock :String = "",
                                //盘点类型
                                var st_type :Int = 0,
                                var start_time :Int = 0,
                                ): Parcelable

