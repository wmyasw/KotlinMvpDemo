package com.wmy.kotlin.demo.mvp

import android.location.Location
import com.hazz.kotlinmvp.api.UrlConstant
import com.wmy.kotlin.demo.http.EasyRetrofit
import com.wmy.kotlin.demo.http.ResponseCallBack
import com.wmy.kotlin.demo.module.WeatherBean

/**
 * 主页业务逻辑处理器
 *@author：wmyasw
 */
 class RecyclePresenter(override var view: RecycleContract.View?) : RecycleContract.Presenter<RecycleContract.View>{
    var list= mutableListOf<String>()
    override fun requestHomeData() {


        list.add("231")
        list.add("qweqwe")
        list.add("asd ad")
        list.add("asdasd")
        list.add("as sad")
        list.add("asdads")
        view?.setHomeData(list)
    }



    override fun start() {
        view?.let { attachView(it) }
    }


}