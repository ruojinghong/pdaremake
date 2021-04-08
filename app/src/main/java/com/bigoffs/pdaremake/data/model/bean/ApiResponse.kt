package com.bigoffs.pdaremake.data.model.bean

import me.hgj.jetpackmvvm.network.BaseResponse

data class ApiResponse<T>(val code: String, val msg: String, val data: T):BaseResponse<T>() {
    override fun isSucces(): Boolean = code == "200"

    override fun getResponseData(): T  = data

    override fun getResponseCode(): String  = code

    override fun getResponseMsg(): String = msg
}