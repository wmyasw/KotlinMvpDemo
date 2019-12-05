package com.wmy.kotlin.mvvm.theme.entity

import ListSelectorAttr
import android.util.Log

object AttrFactory {
    const val BACKGROUND = "background"
    const val TEXT_COLOR = "textColor"
    const val COLOR = "color"
    const val LIST_SELECTOR = "listSelector"
    const val DIVIDER = "divider"
    const val TITLETEXTCOLOR = "titleTextColor"
    operator fun get(attrName: String, attrValueRefId: Int, attrValueRefName: String?, typeName: String?): SkinItem? {
        Log.e("AttrFactory", "attrName：$attrName")
        var mSkinAttr: SkinItem? = null
        if (BACKGROUND == attrName) {
            mSkinAttr = BackgroundAttr()
        } else if (TEXT_COLOR == attrName) {
            mSkinAttr = TextColorAttr()
        } else if (LIST_SELECTOR == attrName) {
            mSkinAttr = ListSelectorAttr()
        } else if (DIVIDER == attrName) {
            mSkinAttr = DividerAttr()
        } else if (COLOR == attrName) {
            mSkinAttr = ColorAttr()
        } else if (TITLETEXTCOLOR == attrName) {
            mSkinAttr = TextColorAttr()
        }
        mSkinAttr!!.name = attrName
        mSkinAttr.resId = attrValueRefId
        mSkinAttr.entryName = attrValueRefName
        mSkinAttr.typeName = typeName
        return mSkinAttr
    }

    /**
     * Check whether the attribute is supported
     *
     * @param attrName
     * @return true : supported <br></br>
     * false: not supported
     */
    fun isSupportedAttr(attrName: String): Boolean {
        if (BACKGROUND == attrName) {
            return true
        }
        if (TEXT_COLOR == attrName) {
            return true
        }
        if (LIST_SELECTOR == attrName) {
            return true
        }
        if (DIVIDER == attrName) {
            return true
        }
        if (TITLETEXTCOLOR == attrName) {
            return true
        }
        return if (COLOR == attrName) {
            true
        } else false
    }
}