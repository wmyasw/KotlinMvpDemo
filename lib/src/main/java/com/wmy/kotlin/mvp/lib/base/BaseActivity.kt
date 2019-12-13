package com.wmy.kotlin.mvp.lib.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.wmy.kotlin.mvp.lib.R
import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract
import kotlinx.android.synthetic.main.activity_common.*


/**
 * 基础类
 *@author：wmyasw
 */
abstract class BaseActivity<P : BaseContract.Presenter<V>, V> : AppCompatActivity() {
    var mPresenter: P? = null
    var srl_refresh: SmartRefreshLayout? = null
    var toolbar: androidx.appcompat.widget.Toolbar? = null
    //    var toolbar: Toolbar? = null
    private var mCenterCommonContentView: View? = null

    fun <T> startActivity1(clazz: Class<T>) {
        startActivity(Intent(this, clazz))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = initPresenter()
        //逻辑处理 判断是否使用公共的toolbar，如果使用 则应用母版逻辑，如果不使用，则页面自行处理，基类不做干预
        if (isShowActionBar()) {
            setContentView(R.layout.activity_common)
            addSubView(layoutId())
            srl_refresh = findViewById(R.id.srl_refresh)
            toolbar = findViewById(R.id.toolbar)
            srl_refresh!!.setEnableLoadMore(false)
            initActionBar()

        } else {
            setContentView(layoutId())
        }
        init()
        initData()
        start()

    }


    /**
     * 初始化标题栏
     */
    protected open fun initActionBar() {
        if (toolbar != null) {
            toolbar!!.setTitle(getTitle())

            setSupportActionBar(toolbar)

            toolbar?.setNavigationOnClickListener(object : OnClickListener {
                override fun onClick(p0: View?) {
                    onBackPressed()
                }

            })
            supportActionBar!!.setDisplayShowTitleEnabled(true)
            //给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            //使自定义的普通View能在title栏显示，即actionBar.setCustomView能起作用，对应ActionBar.DISPLAY_SHOW_CUSTOM
            supportActionBar!!.setDisplayShowCustomEnabled(false)
            //这个小于4.0版本的默认值为true的。但是在4.0及其以上是false,决定左上角的图标是否可以点击。。
            supportActionBar!!.setHomeButtonEnabled(true)
            //使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标，
            // 对应id为android.R.id.home，对应ActionBar.DISPLAY_SHOW_HOME
            //其中setHomeButtonEnabled和setDisplayShowHomeEnabled共同起作用，
            //如果setHomeButtonEnabled设成false，即使setDisplayShowHomeEnabled设成true，图标也不能点击
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            //对应ActionBar.DISPLAY_SHOW_TITLE。
            supportActionBar!!.setDisplayUseLogoEnabled(false)
        }
//        toolbar?.setTitle(title)
    }

    /**
     * 设置title
     */
    open fun setTitle(title: String) {
        toolbar!!.setTitle(title)
    }

    /**
     * 是否使用ActionBar
     */
    open fun isShowActionBar(): Boolean {
        return true
    }

    //初始化逻辑对象
    open fun initPresenter(): P? {
        return null
    }

    /**
     * 初始化view
     */
    abstract fun init()

    /**
     * 布局id
     */
    abstract fun layoutId(): Int

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 开始加载
     */
    abstract fun start()

    /**
     * 打卡软键盘
     */
    fun openKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    /**
     * 根据layoutid 添加内容布局
     */
    fun addSubView(resId: Int) {
        if (resId <= 0) return
        loadChildLayout(center_common as ViewGroup, resId)
    }


    /**
     * 加载子类布局
     */
    fun loadChildLayout(view: ViewGroup, childResId: Int) {
        val laInflater = LayoutInflater.from(view.context)
        if (LinearLayout::class.java.isAssignableFrom(view.javaClass)) {
            val layout = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            view.addView(laInflater.inflate(childResId, null), layout)
        }
        if (RelativeLayout::class.java.isAssignableFrom(view.javaClass)) {
            val layout = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            view.addView(laInflater.inflate(childResId, null), layout)
        }
//        if (AbsoluteLayout::class.java.isAssignableFrom(view.javaClass)) {
//            val layout = AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT, AbsoluteLayout.LayoutParams.FILL_PARENT, 0, 0)
//            view.addView(laInflater.inflate(childResId, null), layout)
//        }
        if (FrameLayout::class.java.isAssignableFrom(view.javaClass)) {
            val layout = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            view.addView(laInflater.inflate(childResId, null), layout)
        }
    }

}