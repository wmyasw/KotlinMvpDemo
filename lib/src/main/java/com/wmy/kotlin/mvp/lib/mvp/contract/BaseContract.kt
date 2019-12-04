package com.wmy.kotlin.mvp.lib.mvp.contract

import com.wmy.kotlin.mvp.lib.mvp.IBasePresenter
import com.wmy.kotlin.mvp.lib.mvp.IBaseVIew

interface BaseContract {
     interface View:IBaseVIew {
        val isNetworkConnected: Boolean

        fun showError(message: String?)
    }

    interface Presenter<V> :IBasePresenter<V>{
        fun attachView(view: V)
        fun detachView()
    }
}