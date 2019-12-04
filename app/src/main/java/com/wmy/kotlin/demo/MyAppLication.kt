package com.wmy.kotlin.demo

import android.content.Context
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.*
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.wmy.kotlin.mvp.lib.base.BaseApplication
import com.wmy.kotlin.mvvm.theme.SkinManager

/**
 *
 *@author：wmyasw
 */
class MyAppLication  : BaseApplication() {
    init {
//    设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(object : DefaultRefreshHeaderCreator {


            override fun createRefreshHeader(context: Context, layout: RefreshLayout): RefreshHeader {
                layout.setPrimaryColorsId(R.color.transparent, android.R.color.white)//全局设置主题颜色
                return ClassicsHeader(context)  //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        })
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(object : DefaultRefreshFooterCreator {

            override fun createRefreshFooter(context: Context, layout: RefreshLayout): RefreshFooter {
                return ClassicsFooter(context).setDrawableSize(20F)
            }
        })
    }


    override fun onCreate() {
        super.onCreate()
        SkinManager.instance.init(this)
    }
}