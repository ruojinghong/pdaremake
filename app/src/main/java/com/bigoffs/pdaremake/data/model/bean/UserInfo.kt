package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(var uid:String  = "admin",
                    var username:String,
                    var nickname:String): Parcelable
