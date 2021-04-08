package com.bigoffs.pdaremake.app.network

import com.bigoffs.pdaremake.app.util.CacheUtil
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author: sxy
 * @date: 2021/3/19
 * @description:自定义头部参数拦截器，传入heads
 */
class MyHeaderInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("Content-Type","application/json;charset=UTF-8")
        return chain.proceed(builder.build())
    }
}