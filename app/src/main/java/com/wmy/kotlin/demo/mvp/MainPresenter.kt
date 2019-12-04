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
 class MainPresenter(override var view: HomeContract.View?) : HomeContract.Presenter<HomeContract.View>{

    lateinit var location:Location
    override fun start() {
        var lat = location.latitude
        var lon = location.longitude
        var map = hashMapOf("lat" to lat
                , "lon" to lon
                , "token" to "443847fa1ffd4e69d929807d42c2db1b"
        )
        EasyRetrofit.instance.post(UrlConstant.weather_cast_3_days, map, object : ResponseCallBack<WeatherBean> {
            override fun onSuccess(response: WeatherBean) {
                view?.setHomeData(response)
            }

            override fun onError(throwable: Throwable) {
                view?.showError("加载失败",22)
            }

        })
    }

    /**
     * 设置请求参数
     */
    override fun requestHomeData(location: Location) {
        this.location=location

    }

    /**
     * 加入对象监听
     */
    override fun attachView(view: HomeContract.View) {
        this.view=view
    }

    /**
     * 删除对象
     */
    override fun detachView() {
        if(view!=null) {
            view=null
        }
    }



}