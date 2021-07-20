package me.hgj.jetpackmvvm.network

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　:自定义错误信息异常
 */
class AppException : Exception {

    var msg: String //错误消息
    var code: Int = 0   //错误码
    var errorLog: String? //错误日志

    constructor(errCode: Int, error: String?, errorLog: String? = "") : super(error) {
        this.msg = error ?: "请求失败，请稍后再试"
        this.code = errCode
        this.errorLog = errorLog?:this.msg
    }

    constructor(error: Error,e: Throwable?) {
        code = error.getKey()
        msg = error.getValue()
        errorLog = e?.message
    }
}