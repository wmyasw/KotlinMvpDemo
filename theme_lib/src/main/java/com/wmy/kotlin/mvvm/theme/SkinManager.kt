package com.wmy.kotlin.mvvm.theme

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.util.Log
import androidx.core.content.ContextCompat
import com.wmy.kotlin.mvvm.theme.listener.ILoaderListener
import com.wmy.kotlin.mvvm.theme.listener.ISkinLoader
import com.wmy.kotlin.mvvm.theme.listener.ISkinUpdate
import java.io.File

/**
 *
 *@author：wmyasw
 */
class SkinManager private constructor() : ISkinLoader {
//    init {
//        throw UnsupportedOperationException("u can't instantiate me...")
//    }

    fun isExternalSkin(): Boolean {
        return !isDefaultSkin && mResources != null
    }

    companion object {
        private var context: Context? = null
        private var mapp: Application? = null
        private var lifecycleCallback: SkinActivityLifecycleCallBack? = null
        //皮肤包名
        private var skinPackageName: String? = null
        //获取皮肤的资源对象
        private var mResources: Resources? = null
        //资源存储路径
        private var skinPath: String? = null
        //观察者集合
//    private val skinObservers: List<ISkinUpdate>? = null
        //是否默认皮肤
        private var isDefaultSkin = false




        //实现懒加载的线程安全
        val instance: SkinManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinManager()
        }


    }
    fun init(app: Application) {

        mapp = app
        context = app.applicationContext
        lifecycleCallback = SkinActivityLifecycleCallBack()
        mapp!!.registerActivityLifecycleCallbacks(lifecycleCallback)
        mResources= context!!.resources
        loadSkin()
    }
    /**
     * 根据传进来的id 去匹配资源对象，如果有类型和名字一样的就返回
     * @param resId
     * @return
     */
    fun getColor(resId: Int): Int {
        Log.d("ee","resId = $resId")
        Log.d("ee","mResources = $mResources")
        if (mResources == null) return resId
        //替换主题资源属性名
        val resourceEntryName = context!!.resources.getResourceEntryName(resId)
        //替换主题资源属性类型 如 color 、mipmap drawable
        val resourceTypeName = context!!.resources.getResourceTypeName(resId)
        //identifier 是资源和名字类型匹配的id
        val identifier = mResources!!.getIdentifier(resourceEntryName, resourceTypeName, skinPackageName)
        Log.d("ee","identifier = $identifier")

        return if (identifier == 0) resId else mResources!!.getColor(identifier)//ContextCompat.getColor(context!!, identifier) mResources

    }

    /**
     * 从皮肤插件中获取drawable 的资源id
     * @param resId
     * @return
     */
    fun getDrawable(resId: Int): Drawable? {
        if (mResources == null) return context?.let { ContextCompat.getDrawable(it, resId) }
        //替换主题资源属性名
        val resourceEntryName = context!!.resources.getResourceEntryName(resId)
        //替换主题资源属性类型 如 color 、mipmap drawable
        val resourceTypeName = context!!.resources.getResourceTypeName(resId)
        //identifier 是资源和名字类型匹配的id
        val identifier = mResources!!.getIdentifier(resourceEntryName, resourceTypeName, skinPackageName)
        return if (identifier == 0) ContextCompat.getDrawable(context!!, resId) else ContextCompat.getDrawable(context!!, identifier)
    }

    /**
     * 返回需要替换的资源id
     * @param resId
     * @return
     */
    fun getResID(resId: Int): Int {
        if (mResources == null) return resId
        //替换主题资源属性名
        val resourceEntryName = context!!.resources.getResourceEntryName(resId)
        //替换主题资源属性类型 如 color 、mipmap drawable
        val resourceTypeName = context!!.resources.getResourceTypeName(resId)
        //identifier 是资源和名字类型匹配的id
        val identifier = mResources!!.getIdentifier(resourceEntryName, resourceTypeName, skinPackageName)
        return if (identifier == 0) identifier else identifier
    }

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。
     * 无皮肤包资源返回默认主题颜色
     * @author pinotao
     * @param resId
     * @return
     */
    fun convertToColorStateList(resId: Int): ColorStateList? {
        Log.e("attr1", "convertToColorStateList")
        var isExtendSkin = true
        if (mResources == null || isDefaultSkin) {
            isExtendSkin = false
        }
        val resName = context!!.resources.getResourceEntryName(resId)
        Log.e("attr1", "resName = $resName")
        if (isExtendSkin) {
            Log.e("attr1", "isExtendSkin")
            val trueResId = mResources!!.getIdentifier(resName, "color", skinPackageName)
            Log.e("attr1", "trueResId = $trueResId")
            var trueColorList: ColorStateList? = null
            if (trueResId == 0) { // 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList
                try {
                    return ContextCompat.getColorStateList(context!!, resId)
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                    Log.e("", "resName = " + resName + " NotFoundException : " + e.message)
                }
            } else {
                try {
                    trueColorList = ContextCompat.getColorStateList(context!!, trueResId)
                    Log.e("attr1", "trueColorList = $trueColorList")
                    return trueColorList
                } catch (e: NotFoundException) {
                    e.printStackTrace()
                    Log.e("", "resName = " + resName + " NotFoundException :" + e.message)
                }
            }
        } else {
            try {
                return ContextCompat.getColorStateList(context!!, resId)
            } catch (e: NotFoundException) {
                e.printStackTrace()
                Log.w("attr1", "resName = " + resName + " NotFoundException :" + e.message)
            }
        }
        val states = Array(1) { IntArray(1) }
        return ColorStateList(states, intArrayOf(ContextCompat.getColor(context!!, resId)))
    }

//    /**
//     * 初始化管理器
//     * @param ctx 上下文对象
//     */
//    fun init(ctx: Context) { //获取application 上下文对象
//        context = ctx.applicationContext as Application?
//    }

    /**
     * 重置皮肤
     */
    fun reDefaultTheme() { //重置默认皮肤地址
        SkinConfig.saveSkinPath(context, SkinConfig.DEFALT_SKIN)
        isDefaultSkin = true
//        if( SkinFactory.instance!=null){
//            mResources = SkinFactory.context!!.resources
//        }
        mResources = null
        // 用于访问内置资源

//        mResources = context!!.resources
        notifySkinUpdate()
    }

    /**
     * 加载默认皮肤
     */
    fun loadSkin() {
        val skin: String = SkinConfig.getCustomSkinPath(context)
        loadSkin(skin, null)
    }

    /**
     * 设置制定皮肤
     * @param path
     */
    fun loadSkin(path: String?) {
        loadSkin(path, null)
    }

    /**
     * 设置默认皮肤并返回监听
     * @param callback 回调
     */
    fun loadSkin(callback: ILoaderListener?) {
        val skin: String = SkinConfig.getCustomSkinPath(context)
        if (SkinConfig.isDefaultSkin(context)) {
            return
        }
        loadSkin(skin, callback)
    }
    /**
     * 设置指定皮肤
     * @param path 地址
     * @param callback 回调
     */
    @SuppressLint("StaticFieldLeak")
    fun loadSkin(path: String?, callback: ILoaderListener?) {
        object : AsyncTask<String?, Void?, Resources?>() {
            //加载开始
            override fun onPreExecute() {
                callback?.onStart()
            }

            override fun doInBackground(vararg p0: String?): Resources? {
                return try {
                    if (p0.size == 1) {
                        val skinPkgPath = p0[0]
                        val file = File(skinPkgPath)
                        if (file == null || !file.exists()) {
                            return null
                        }
                        val mPm = context!!.packageManager
                        val mInfo = mPm.getPackageArchiveInfo(skinPkgPath, PackageManager.GET_ACTIVITIES)
                        skinPackageName = mInfo.packageName
                        val assetManager = AssetManager::class.java.newInstance()
                        val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
                        addAssetPath.invoke(assetManager, skinPkgPath)
                        val superRes = context!!.resources
                        val skinResource = Resources(assetManager, superRes.displayMetrics, superRes.configuration)
                        SkinConfig.saveSkinPath(context, skinPkgPath)
                        skinPath = skinPkgPath
                        isDefaultSkin = false
                        return skinResource
                    }
                    null
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }

            override fun onPostExecute(result: Resources?) {
                mResources = result
                if (mResources != null) {
                    callback?.onSuccess()
                    notifySkinUpdate()
                } else {
                    isDefaultSkin = true
                    callback?.onFailed()
                }
            }

        }.execute(path)
    }
    override fun attach(observer: ISkinUpdate?) {
//        if (skinObservers == null) {
//            skinObservers = ArrayList<ISkinUpdate>()
//        }
//        if (!skinObservers.contains(skinObservers)) {
//            skinObservers.add(observer)
//        }
    }

    override fun detach(observer: ISkinUpdate?) {
//        if (skinObservers == null) return
//        if (skinObservers.contains(observer)) {
//            skinObservers.remove(observer)
//        }
    }

    override fun notifySkinUpdate() {

        SkinFactory.instance?.apply()
//        if (skinObservers == null) return
//        for (observer in skinObservers) {
//            observer.onThemeUpdate()
//        }
    }
     fun claan() {
//        context.resources
//        if (skinObservers == null) return
//        for (observer in skinObservers) {
//            observer.onThemeUpdate()
//        }
    }

}