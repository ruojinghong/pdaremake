package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StocktakingListBean(
                                var name :String = "未知",
                                var id :String = "未知",
                                var book_stock_total :String = "未知",
                                var real_stock_total :String = "未知",
                                var st_type_name :String = "未知",
                                var start_time :String = "未知",
                                //盘点类型
                                var w_id :Int = 0,
                                var st_type : Int = 1,
                                //1初盘 2复盘
                                var st_status : Int = 1,

                                ): Parcelable

