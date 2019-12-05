package com.wmy.kotlin.mvvm.theme

import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.wmy.kotlin.mvvm.theme.entity.AttrFactory
import com.wmy.kotlin.mvvm.theme.entity.SkinItem
import com.wmy.kotlin.mvvm.theme.entity.SkinView
import com.wmy.kotlin.mvvm.theme.utils.ListUtils
import java.lang.reflect.Constructor
import java.util.*


/**
 * 换肤工厂类
 * 获取需要换肤的控件
 *@author：wmyasw
 */
class SkinFactory private constructor(ctx:Context):LayoutInflater.Factory2 {
    private val DEBUG = true
    private val viewList: MutableList<SkinView> =ArrayList<SkinView>()
    private var mSkinItems: MutableList<SkinItem> = ArrayList<SkinItem>()
    var prifixList = arrayOf(
            "android.widget.",
            "android.view.",
            "android.webkit."

    )
    init{
        context=ctx
    }
    companion object {
        var context:Context?=null
        @Volatile
        var instance: SkinFactory? = null

        fun getInstance(c: Context): SkinFactory {
            if (instance == null) {
                synchronized(SkinFactory::class) {
                    if (instance == null) {
                        instance = SkinFactory(c)
                    }
                }
            }
            return instance!!
        }
    }



//        //实现懒加载的线程安全
//        val instance: SkinFactory() by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
//            SkinFactory()
//        }


    override fun onCreateView(parent: View?, s: String, context: Context, attributeSet: AttributeSet): View? {
//        Log.e("SkinFactory", "----------------------->$s")
        val isSkinEnable = attributeSet.getAttributeBooleanValue( SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false)
        if (!isSkinEnable) {
            return null
        }
        Log.e("SkinFactory", "----------------------->$s")
        var view: View? = null
        if (s.contains(".")) {
            view = onCreateView(s, context, attributeSet)
        } else {
            for (s1 in prifixList) {
                view = onCreateView(s1, context, attributeSet)
                if (view != null) break
            }
        }
        //收集所有需要换肤的控件
        view?.let { parseView(it, s, attributeSet) }
        return view
    }

    /**
     * 如果控件被实例化，进行判断是否满足换肤需求，然后存入换肤队列
     *
     * @param view
     * @param s
     * @param attrs
     */
    private fun parseView(view: View, s: String, attrs: AttributeSet) {
        val items: MutableList<SkinItem> = ArrayList<SkinItem>()
        for (i in 0 until attrs.attributeCount) { //属性名
            val attrName = attrs.getAttributeName(i)
            //属性id
            val attrValue = attrs.getAttributeValue(i)
            //判断属性是否包含 替换条件的属性类型
            if (AttrFactory.isSupportedAttr(attrName)) { //资源id
                val resId = attrValue.substring(1).toInt()
                //资源属性名成
                val resourceEntryName = view.resources.getResourceEntryName(resId)
                //资源类型 color
                val resourceTypeName = view.resources.getResourceTypeName(resId)
                val skinItem: SkinItem = AttrFactory.get(attrName, resId, resourceEntryName, resourceTypeName)!!
                //new SkinItem(attrName, resId, resourceTypeName, resourceEntryName);
                if (skinItem != null) {
                    items.add(skinItem)
                }
                mSkinItems=items
            }
            if (items.size > 0) {
                val skinView = SkinView(view, items)
                viewList.add(skinView)
//                SkinManager.instance.init(context as Application)
                if (SkinManager.instance.isExternalSkin()) {
                    skinView.apply()
                }
            }
        }
    }

    /**
     * @param s            view name
     * @param context      上下文
     * @param attributeSet 属性集
     * @return
     */
    override fun onCreateView(s: String, context: Context, attributeSet: AttributeSet): View? {
        var view: View? = null
        try {
            val aClass = context.classLoader.loadClass(s)
            val constructor: Constructor<out View> = aClass.getConstructor(*arrayOf(Context::class.java, AttributeSet::class.java)) as Constructor<out View>
            view = constructor.newInstance(context, attributeSet)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view
    }

    fun apply() {
        for (skinView in viewList) {
            if (skinView == null) continue
            skinView.apply()
        }
    }
    //回收内存
    fun clean() {
        if (ListUtils.isEmpty(viewList)) {
            return
        }
        for (si in viewList) {
            if (si.getView() == null) {
                continue
            }
            si.clean()
        }
    }
}