package com.wmy.kotlin.demo.utils


import android.util.Log
import com.wmy.kotlin.mvp.lib.BuildConfig


/**
 * 日志工具类
 *@author：wmyasw
 */

object LogUtils  {

    /**日志输出级别 0为不输出 */
    private val logMode = Log.ERROR
    private var TAG = "LogUtils"

    fun setTag(s:String){
        TAG=s;
    }

    /**
     * 输出Error信息
     *
     * @param tag
     * 异常信息标识
     * @param msg
     * 异常信息
     */
    fun e(tag: String, msg: String) {

        if (BuildConfig.DEBUG)
            if (Log.ERROR <= logMode)
                Log.e(tag, msg)
    }
    fun e( msg: String) {

        if (BuildConfig.DEBUG)
            if (Log.ERROR <= logMode)
                Log.e(TAG, msg)
    }
    /**
     * 输出警告信息
     *
     * @param tag
     * 错误信息标识
     * @param msg
     * 错误信息
     */
    fun w(tag: String, msg: String) {

        if (BuildConfig.DEBUG)
            if (Log.WARN <= logMode)
                Log.w(tag, msg)
    }

    /**
     * 输出普�?信息
     *
     * @param tag
     * 普�?信息
     * @param msg
     * 异常信息
     */
    fun i(tag: String, msg: String) {

        if (BuildConfig.DEBUG)
            if (Log.INFO <= logMode)
                Log.i(tag, msg)
    }


    /**
     * 输出debug信息
     *
     * @param tag
     * 调试信息标识
     * @param msg
     * 调试信息
     */
    fun d(tag: String, msg: String?) {

        if (BuildConfig.DEBUG)
            if (Log.DEBUG <= logMode)
                Log.d(tag, msg ?: "")
    }

    fun d(msg: String?) {

        if (BuildConfig.DEBUG)
            if (Log.DEBUG <= logMode)
                Log.d(TAG, msg ?: "")
    }


}
