package com.wmy.kotlin.mvp.lib.mvp.contract

import com.wmy.kotlin.mvp.lib.mvp.IBasePresenter
import com.wmy.kotlin.mvp.lib.mvp.IBaseVIew

interface BaseContract {
     interface View:IBaseVIew {
        val isNetworkConnected: Boolean

        fun showError(message: String?)


     }

    interface Presenter<V> :IBasePresenter<V>{
        var view: V?
//        fun attachView(view: V)
//        fun detachView()
        /**
         * 加入对象监听
         */
         fun attachView(view: V) {
            this.view=view
        }

        /**
         * 删除对象
         */
         fun detachView() {
            if(view!=null) view=null
        }

    }
}