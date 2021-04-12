package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (var token:String  = "",
                 var userInfo:UserInfo):Parcelable