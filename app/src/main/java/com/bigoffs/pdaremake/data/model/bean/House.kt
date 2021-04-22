package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class House(val id:Int,val name :String ) :Parcelable
