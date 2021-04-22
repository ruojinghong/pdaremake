package com.bigoffs.pdaremake.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.bigoffs.pdaremake.app.network.apiService
import com.bigoffs.pdaremake.data.model.bean.House
import com.bigoffs.pdaremake.data.model.bean.User
import com.google.gson.Gson
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.MediaType
import okhttp3.RequestBody




/**
 * @author: sxy
 * @date: 2021/3/19
 * @description:
 */
class RequestLoginViewModel : BaseViewModel(){

    //方式1  自动脱壳过滤处理请求结果，判断结果是否成功
    var loginResult = MutableLiveData<ResultState<User>>()
    var detaliResult = MutableLiveData<ResultState<List<User>>>()
    var houseResult = MutableLiveData<ResultState<List<House>>>()

    //方式2  不用框架帮脱壳，判断结果是否成功
//    var loginResult2 = MutableLiveData<ResultState<ApiResponse<UserInfo>>>()


    fun loginReq(username: String, password: String) {
        //1.这种是在 Activity/Fragment的监听回调中拿到已脱壳的数据（项目有基类的可以用）
        val map = HashMap<String, String>()
        map.put("username",username)
        map.put("password",password)
        map.put("appid","pda")
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Gson().toJson(map))
        RetrofitUrlManager.getInstance().putDomain("loginurl", "http://admin.bigoffs.com:9080/");
        request(
            { apiService.login(requestBody) }//请求体
            , loginResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示 可以默认不传
            "正在登录中..."//等待框内容，可以默认不填请求网络中...
        )
        //2.这种是在Activity/Fragment中的监听拿到未脱壳的数据，你可以自己根据code做业务需求操作（项目没有基类的可以用）
        /*requestNoCheck({HttpRequestCoroutine.login(username,password)},loginResult2,true)*/

        //3. 这种是直接在当前ViewModel中就拿到了脱壳数据数据，做一层封装再给Activity/Fragment，如果 （项目有基类的可以用）
        /* request({HttpRequestCoroutine.login(username,password)},{
             //请求成功 已自动处理了 请求结果是否正常
         },{
             //请求失败 网络异常，或者请求结果码错误都会回调在这里
         })*/

        //4.这种是直接在当前ViewModel中就拿到了未脱壳数据数据，（项目没有基类的可以用）
        /*requestNoCheck({HttpRequestCoroutine.login(username,password)},{
            //请求成功 自己拿到数据做业务需求操作
            if(it.errorCode==0){
                //结果正确
            }else{
                //结果错误
            }
        },{
            //请求失败 网络异常回调在这里
        })*/
    }

//    fun getDetail(id: String){
//        val map = HashMap<String, Any>()
//        map.put("shelf_code",id)
//
//        map.put("w_id",1)
//        val requestBody: RequestBody =
//            RequestBody.create(MediaType.parse("application/json"), Gson().toJson(map))
//        request(
//            { apiService.detail(requestBody) }//请求体
//            , detaliResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
//            true,//是否显示等待框，，默认false不显示 可以默认不传
//            "正在登录中..."//等待框内容，可以默认不填请求网络中...
//        )
//    }


    fun getHouseId() {
        //1.这种是在 Activity/Fragment的监听回调中拿到已脱壳的数据（项目有基类的可以用）
        val requestBody: RequestBody =
            RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "")
        request(
            { apiService.getHouse(requestBody) }//请求体
            , houseResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示 可以默认不传
            "正在登录中..."//等待框内容，可以默认不填请求网络中...
        )
    }
}