package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StocktakingListBean(
                                var name :String = "",
                                var id :String = "",
                                var book_stock_total :String = "",
                                var real_stock_total :String = "",
                                var st_type_name :String = "",
                                var start_time :String = "",
                                //盘点类型
                                var w_id :Int = 0,

                                ): Parcelable

