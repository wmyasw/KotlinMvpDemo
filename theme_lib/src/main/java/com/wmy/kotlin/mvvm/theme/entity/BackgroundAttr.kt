package com.wmy.kotlin.mvvm.theme.entity

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Build
import android.util.Log
import android.view.View
import com.wmy.kotlin.mvvm.theme.SkinManager

/**
 *
 *@author：wmyasw
 */
class BackgroundAttr : SkinItem() {

    fun setAnimation(view: View, values: Int) {
        var colorAnimator = ObjectAnimator.ofInt(view, "backgroundColor", values)
        if (resId == values)
            colorAnimator = ObjectAnimator.ofInt(view, "backgroundResource", values)
        //设置动画时间
        colorAnimator.setDuration(500)
        //设置插值器
        colorAnimator.setEvaluator(ArgbEvaluator())
        //设置播放次数为无限
//        colorAnimator.repeatCount = ValueAnimator.INFINITE
//        //播放完成之后反转
//        colorAnimator.repeatMode = ValueAnimator.REVERSE
        colorAnimator.start()
    }

    override fun apply(view: View) {

        if (SkinItem.RES_TYPE_NAME_COLOR == getTypeName()) {
            Log.i("BackgroundAttr", "_________________________________________________________")
            Log.i("BackgroundAttr", "apply as color ：$resId")
            var rid = SkinManager.instance.getColor(resId)
            Log.i("BackgroundAttr", "apply as rid color ：$rid")
            setAnimation(view, SkinManager.instance.getColor(resId))
        } else if (SkinItem.RES_TYPE_NAME_DRAWABLE == getTypeName()) {
            val bgResId: Int = SkinManager.instance.getResID(resId)
            view.setBackgroundResource(bgResId)
            Log.i("BackgroundAttr", "_________________________________________________________")
            Log.i("BackgroundAttr", "apply as drawable：$resId")
            //            Log.i("attr", "bg.toString()  " + bg.toString());
//            Log.i("attr", this.entryName + " 是否可变换状态? : " + bg.isStateful());
        }

    }
}