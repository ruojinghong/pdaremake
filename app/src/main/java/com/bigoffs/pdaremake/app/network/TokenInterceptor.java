package com.bigoffs.pdaremake.app.network;

import android.util.Log;

import com.bigoffs.pdaremake.data.model.bean.TokenBus;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


import java.io.IOException;
import java.nio.charset.Charset;

import me.hgj.jetpackmvvm.network.BaseResponse;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(final Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();

        source.request(Long.MAX_VALUE);
        String respString = source.buffer().clone().readString(Charset.defaultCharset());
//        BaseResponse baseEntity = null;
//        try {
//            baseEntity = new Gson().fromJson(respString, BaseResponse.class);
//        } catch (JsonSyntaxException e) {
//        }
//        if (baseEntity != null && baseEntity.getCode() == 1008) {
//            UserManager.clearUserInfo();
//            LiveBus.getDefault().absPostEvent(CommEventUtil.TOKEN_ABNORMAL, new BaseResponse());
//            LiveBus.getDefault().absPostEvent(CommEventUtil.LOGOUT_SUCCESS, new BaseResponse());
//        }


        return response;
    }
}
