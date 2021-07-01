package com.bigoffs.pdaremake.data.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CheckEpcTally(var abnormal_data:TallyAbnormalData,
                         var normal_data:TallyNormalData
                         ):Parcelable
