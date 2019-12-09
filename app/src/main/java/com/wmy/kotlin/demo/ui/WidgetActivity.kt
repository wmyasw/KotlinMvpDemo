package com.wmy.kotlin.demo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wmy.kotlin.demo.R
import com.wmy.kotlin.mvp.lib.base.BaseActivity
import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract.Presenter

class WidgetActivity : BaseActivity<Presenter<Any>, Any>() {
    override fun initPresenter(): Presenter<Any>? {
        return null
    }

    override fun init() {
    }

    override fun layoutId(): Int =R.layout.activity_widget

    override fun initData() {
    }

    override fun start() {
    }


}
