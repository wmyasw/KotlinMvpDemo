package com.wmy.kotlin.demo.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.*


/**
 * 初始化工具类
 *@author：wmyasw
 */
class Utils private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {

        private var context: Context? = null

        /**
         * 初始化工具类
         *
         * @param context 上下文
         */
        fun init(context: Context) {
            Utils.context = context.applicationContext
                    var app= Utils.context as Application
            app.registerActivityLifecycleCallbacks(object :Application.ActivityLifecycleCallbacks{
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
                    LogUtils.setTag(p0.localClassName)
                }

                override fun onActivityResumed(p0: Activity) {
                }

            })
        }

        /**
         * 获取ApplicationContext
         *
         * @return ApplicationContext
         */
        fun getContext(): Context {
            if (context != null) return context as Context
            throw NullPointerException("u should init first")
        }

        fun dp2px(dp: Int): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                    Resources.getSystem().displayMetrics)
        }

        fun sp2px( spVal:Int): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal.toFloat(),  Resources.getSystem().displayMetrics);
        }
        /**
         * View获取Activity的工具
         *
         * @param view view
         * @return Activity
         */
        fun getActivity(view: View): Activity {
            var context = view.context

            while (context is ContextWrapper) {
                if (context is Activity) {
                    return context
                }
                context = context.baseContext
            }

            throw IllegalStateException("View $view is not attached to an Activity")
        }

        /**
         * 全局获取String的方法
         *
         * @param id 资源Id
         * @return String
         */
        fun getString(@StringRes id: Int): String {
            return context!!.resources.getString(id)
        }

        /**
         * 判断App是否是Debug版本
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        val isAppDebug: Boolean
            get() {
                if (StringUtils.isSpace(context!!.packageName)) return false
                try {
                    val pm = context!!.packageManager
                    val ai = pm.getApplicationInfo(context!!.packageName, 0)
                    return ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                    return false
                }

            }


        /**
         * The `fragment` is added to the container view with id `frameId`. The operation is
         * performed by the `fragmentManager`.
         */
        fun addFragmentToActivity(fragmentManager: FragmentManager,
                                  fragment: Fragment, frameId: Int) {
            checkNotNull(fragmentManager)
            checkNotNull(fragment)
            val transaction = fragmentManager.beginTransaction()
            transaction.add(frameId, fragment)
            transaction.commit()
        }


        fun <T> checkNotNull(obj: T?): T {
            if (obj == null) {
                throw NullPointerException()
            }
            return obj
        }
    }


}