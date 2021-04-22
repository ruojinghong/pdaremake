package me.hgj.jetpackmvvm.network

import androidx.annotation.Nullable
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.Request


/**
 * 通过代理来修改baseUrl
 * desc:代理{@link okhttp3.Call.Factory} 拦截{@link #newCall(Request)}方法
 */
abstract  class CallFactoryProxy(delegate: Call.Factory) : Call.Factory {
    private val NAME_BASE_URL = "BaseUrlName"
    private val delegate: Call.Factory = delegate

    override fun newCall(request: Request): Call {
        var baseUrl = request.header(NAME_BASE_URL)
        if(baseUrl != null){
            val newHttpUrl: HttpUrl = getNewUrl(baseUrl, request)
            if (newHttpUrl != null) {
                val newRequest = request.newBuilder().url(newHttpUrl).build()
                return delegate.newCall(newRequest)
            }
        }
        return delegate.newCall(request);
    }

    @Nullable
    protected abstract fun getNewUrl(baseUrlName: String?, request: Request?): HttpUrl
}