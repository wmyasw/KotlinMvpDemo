package com.wmy.kotlin.demo.http


interface ResponseCallBack<T> {
    /**
     * 加载数据成功
     *
     * @param response 返回的数据
     */
    fun  onSuccess(response: T)

    /**
     * 加载数据失败
     *
     * @param throwable 错误信息
     */
    fun onError(throwable: Throwable)
}
