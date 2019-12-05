package com.wmy.kotlin.mvvm.theme.entity

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ListView
import com.wmy.kotlin.mvvm.theme.SkinManager


/**
 * @author：@user()
 * @date：$date$ $time$
 */
internal class DividerAttr : SkinItem() {
    var dividerHeight = 1
    override fun apply(view: View) {
        if (view is ListView) {
            val tv = view
            if (RES_TYPE_NAME_COLOR == typeName) {
                val color: Int = SkinManager.instance.getColor(resId)
                val sage = ColorDrawable(color)
                tv.divider = sage
                tv.dividerHeight = dividerHeight
            } else if (RES_TYPE_NAME_DRAWABLE == typeName) {
                tv.divider = SkinManager.instance.getDrawable(resId)
            }
        }
    }
}