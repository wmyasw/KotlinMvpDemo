package com.wmy.kotlin.demo.mvp

import android.location.Location
import com.wmy.kotlin.demo.module.WeatherBean
import com.wmy.kotlin.mvp.lib.mvp.IBasePresenter
import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract

interface HomeContract{

    interface View : BaseContract.View {

        /**
         * 设置第一次请求的数据
         */
        fun setHomeData( w: WeatherBean)

        /**
         * 设置加载更多的数据
         */
        fun setMoreData()

        /**
         * 显示错误信息
         */
        fun showError(msg: String, errorCode: Int)


    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        var view :V?
        /**
         * 获取首页精选数据
         */
        open fun requestHomeData(location: Location)

    }


}