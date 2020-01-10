//package com.wmy.kotlin.mvp.lib.mvp
//
//import androidx.annotation.Keep
//import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract
//
///**
// *
// *@author：wmyasw
// */
//open class BasePresenter<V > : BaseContract.Presenter<BaseContract.View> {
//   override var view: V? = null
//
//
//    override fun attachView(view: BaseContract.View) {
//        this.view=view as V
//    }
//
//    override fun detachView() {
//        if(view!=null)view
//    }
//
//    override fun start() {
//    }
//
//}