package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo (var userId:String  = "admin",
                    var trueName:String,
                    var accessToken:String):Parcelable