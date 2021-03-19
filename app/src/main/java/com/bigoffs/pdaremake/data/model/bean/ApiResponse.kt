package com.bigoffs.pdaremake.data.model.bean

import me.hgj.jetpackmvvm.network.BaseResponse

data class ApiResponse<T>(var status:Int,var t:T):BaseResponse<T>() {
    override fun isSucces(): Boolean = status == 1

    override fun getResponseData(): T  = t

    override fun getResponseCode(): Int  = -1

    override fun getResponseMsg(): String = "暂未实现"
}