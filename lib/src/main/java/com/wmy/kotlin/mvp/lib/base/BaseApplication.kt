package com.wmy.kotlin.mvp.lib.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.wmy.kotlin.demo.utils.LogUtils
import com.wmy.kotlin.demo.utils.Utils
import kotlin.properties.Delegates


/**
 * @author wmy
 * @Description:   要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取Context的方法必须为:Utils.getContext()，不允许其他写法；
 * @FileName: BaseApplication
 * @Date 2018/6/8/008 10:14
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ins = this
        //        Logger.init("pattern").logLevel(LogLevel.FULL);
        Utils.init(this)
    }


    companion object {

        val ROOT_PACKAGE = "com.wmy.module"

        var ins: BaseApplication by Delegates.notNull()
            private set
    }


}
