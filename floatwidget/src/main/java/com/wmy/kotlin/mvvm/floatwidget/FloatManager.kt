package com.wmy.kotlin.mvvm.floatwidget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Outline
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView


/**
 *
 *@author：wmyasw
 */
class FloatManager {
    companion object {
        @Volatile
        private var mInstance: FloatManager? = null
        val instance: FloatManager?
            get() {
                if (mInstance == null) {
                    synchronized(FloatManager::class.java) {
                        if (mInstance == null) {
                            mInstance = FloatManager()
                        }
                    }
                }
                return mInstance
            }
    }

    var width = 0
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("CheckResult")
    fun showWindow(context: Context) {
        var mFloatingWindow = FloatView(context)
        mFloatingWindow.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(context,"asdasdasd", Toast.LENGTH_SHORT).show()
            }

        })
        mFloatingWindow.setOnLongClickListener(object: View.OnLongClickListener {

            override fun onLongClick(p0: View?): Boolean {
                Toast.makeText(context,"Longgggggggggg", Toast.LENGTH_SHORT).show()
                return true
            }

        })

    }

}