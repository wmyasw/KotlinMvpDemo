package com.wmy.kotlin.mvvm.theme.entity

import android.os.Build
import android.util.Log
import android.view.View
import com.wmy.kotlin.mvvm.theme.SkinManager

/**
 *
 *@author：wmyasw
 */
class BackgroundAttr : SkinItem() {
    override fun apply(view: View) {

        if (SkinItem.RES_TYPE_NAME_COLOR == getTypeName()) {
            Log.i("BackgroundAttr", "_________________________________________________________")
            Log.i("BackgroundAttr", "apply as color ：$resId")
            var rid = SkinManager.instance.getColor(resId)
            Log.i("BackgroundAttr", "apply as rid color ：$rid")
            if (resId == rid) {
                view.setBackgroundResource(rid)
            } else
                view.setBackgroundColor(rid)
//            view.setBackgroundResource(resId)
        } else if (SkinItem.RES_TYPE_NAME_DRAWABLE == getTypeName()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.background = SkinManager.instance.getDrawable(resId)
            } else {
                val bgResId: Int = SkinManager.instance.getResID(resId)
                view.setBackgroundResource(bgResId)
            }
            Log.i("BackgroundAttr", "_________________________________________________________")
            Log.i("BackgroundAttr", "apply as drawable：$resId")
            //            Log.i("attr", "bg.toString()  " + bg.toString());
//            Log.i("attr", this.entryName + " 是否可变换状态? : " + bg.isStateful());
        }

    }
}