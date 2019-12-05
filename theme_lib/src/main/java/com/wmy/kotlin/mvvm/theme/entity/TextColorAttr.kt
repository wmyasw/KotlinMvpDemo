package com.wmy.kotlin.mvvm.theme.entity

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.TextView
import com.wmy.kotlin.mvvm.theme.SkinManager

/**
 * 替换文本颜色
 *@author：wmyasw
 */
class TextColorAttr : SkinItem(){
    @SuppressLint("NewApi")
    override fun apply(view: View?) {
        Log.e("TextColorAttr", "typeName:$typeName")
        Log.e("TextColorAttr", "typeName:$view")
        if (view is TextView) {
            if (SkinItem.RES_TYPE_NAME_COLOR == typeName) {
                Log.e("TextColorAttr", "TextView: ")
                view.setTextColor(SkinManager.instance.convertToColorStateList(resId))
            }
        }
        if (view is androidx.appcompat.widget.Toolbar) {
            if (SkinItem.RES_TYPE_NAME_COLOR == typeName) {
                Log.e("TextColorAttr", "Toolbar:")
                view.setTitleTextColor(resId)
            }
        }
        if (view is androidx.appcompat.widget.AppCompatTextView) {
            if (SkinItem.RES_TYPE_NAME_COLOR == typeName) {
                Log.e("TextColorAttr", "Toolbar:")
                view.setTextColor(resId)
            }
        }
    }
}