package com.wmy.kotlin.demo.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wmy.kotlin.demo.R
import com.wmy.kotlin.mvp.lib.base.BaseActivity
import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract
import kotlinx.android.synthetic.main.activity_zy.*
import android.content.ClipData
import android.content.Context
import android.os.Build
import android.text.InputType
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.wmy.kotlin.demo.utils.LogUtils
import com.wmy.kotlin.mvp.lib.utils.*
import kotlinx.android.synthetic.main.content_shake.*
import java.io.File

class ZYActivity : BaseActivity<BaseContract.Presenter<Any>, Any>() {


    @ExperimentalStdlibApi
    override fun init() {
        editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        var imm: InputMethodManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false)
        }
//        editText.setOnFocusChangeListener(object : View.OnFocusChangeListener {
//            override fun onFocusChange(p0: View?, p1: Boolean) {
//                openKeyBord(editText, applicationContext)
//            }
//
//        })


        button3.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var content = editText.text.toString()

                LogUtils.e(content.autoDecodeOrEncode())
                LogUtils.e(content.encodeHexagram())
                textView3.setText(content.encodeHexagram())
            }

        })
        button4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var content = editText.text.toString()

                LogUtils.e(textView3.text.toString().autoDecodeOrEncode())
                textView4.setText(textView3.text.toString().decodeHexagram())
            }
        })
        button5.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                var char = textView3.text.toString().toCharArray()
                var rs = char[0].toString()
//                LogUtils.e("button5 ${rs.convertToJieQian()}")
                val base64 = Base64.encodeToString(
                        rs.convertToJieQian().toByteArray(),
                        Base64.URL_SAFE and Base64.NO_WRAP and Base64.NO_PADDING
                )
                LogUtils.e("button5 ${ Base64.decode(
                        base64.toString(),
                        Base64.URL_SAFE and Base64.NO_WRAP and Base64.NO_PADDING
                )}")
                LogUtils.e("button5 $base64")
                textView5.setText(    rs.convertToJieQian())

            }
        })
        button6.setOnClickListener(object : View.OnClickListener {
            @SuppressLint("SetTextI18n")
            override fun onClick(p0: View?) {
                var content = editText.text.toString()
//                LogUtils.e("button5 ${content.convertToBiHua()}")
                textView10.setText("${content.getAssetsJson(applicationContext)} 笔画")
            }
        })
        button7.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var content = editText.text.toString()

                textView12.setText("${content.toPinYin()} ")
            }
        })
        btn_gossip.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var content = editText.text.toString()

                textView5.setText("${content.getAfterGossip()} ")
            }
        })
    }

    override fun layoutId(): Int = R.layout.activity_zy


    override fun start() {

    }
}
