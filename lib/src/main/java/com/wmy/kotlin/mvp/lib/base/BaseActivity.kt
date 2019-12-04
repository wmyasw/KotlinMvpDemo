package com.wmy.kotlin.mvp.lib.base

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract

/**
 * 基础类
 *@author：wmyasw
 */
abstract class BaseActivity<P : BaseContract.Presenter<V>, V> : AppCompatActivity() {
    lateinit var mPresenter: P
//    protected var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
//        ThemeUtils.setBaseTheme(this)
        super.onCreate(savedInstanceState)
        if (layoutId() > 0) {
            setContentView(layoutId())
        }
        //        ButterKnife.bind(this);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar)
//        }
        mPresenter = initPresenter()
        init()
        initData()
        start()

    }

    protected open fun setupToolBar(hideTitle: Boolean) {
        val actionBar = supportActionBar
        if (actionBar != null) { //            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            if (hideTitle) { //隐藏Title
                actionBar.setDisplayShowTitleEnabled(false)
            }
        }
    }

    protected open fun setTitle(title: String) {
//        toolbar?.setTitle(title)
    }


    //初始化逻辑对象
    abstract fun initPresenter(): P

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


}