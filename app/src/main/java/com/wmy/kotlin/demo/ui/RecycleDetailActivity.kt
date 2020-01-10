package com.wmy.kotlin.demo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wmy.kotlin.demo.R
import com.wmy.kotlin.mvp.lib.base.BaseActivity
import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract

class RecycleDetailActivity :  BaseActivity<BaseContract.Presenter<Any>, Any>() {

    override fun init() {
        srl_refresh!!.setEnableRefresh(false)


    }

    override fun layoutId(): Int =R.layout.activity_recycle_detail

    override fun start() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
