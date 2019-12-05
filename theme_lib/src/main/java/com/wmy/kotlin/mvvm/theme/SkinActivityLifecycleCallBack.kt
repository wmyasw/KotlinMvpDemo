package com.wmy.kotlin.mvvm.theme

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat

/**
 * 监听进程间 activity的声明状态
 *@author：wmyasw
 */
class SkinActivityLifecycleCallBack : Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        val layoutInflater = LayoutInflater.from(p0)
        try {
            val field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            field.isAccessible = true
            field.setBoolean(layoutInflater, false)
            LayoutInflaterCompat.setFactory2(layoutInflater, SkinFactory.getInstance(p0))
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }


    override fun onActivityResumed(p0: Activity) {
    }
}