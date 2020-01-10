package com.wmy.kotlin.demo.ui

import android.os.Build
import com.hazz.kotlinmvp.utils.AppUtils
import com.wmy.kotlin.demo.R
import com.wmy.kotlin.demo.utils.LogUtils
import com.wmy.kotlin.mvp.lib.base.BaseActivity
import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : BaseActivity<BaseContract.Presenter<Any>, Any>() {
    override fun init() {

        t_package_name.setText(AppUtils.getPackageName(this))
        t_app_version_code_name.setText(AppUtils.getVerCode(this).toString() )
        t_app_version_code_name.setText(AppUtils.getVerCode(this).toString() )
        t_app_version_name.setText(AppUtils.getVerName(this))
        t_system_version_code.setText(AppUtils.systemVersion)
        t_phone_kit_code.setText(AppUtils.getMobileModel())
        t_min_sdk_version_code.setText(AppUtils.getMobileModel())
        t_target_version_code.setText(AppUtils.sdkVersion.toString())
        LogUtils.e("// 当前开发代号    \n" +
                "Build.VERSION.CODENAME     ${Build.VERSION.CODENAME}  \n" +
                "// 源码控制版本号  \n" +
                "Build.VERSION.INCREMENTAL   ${Build.VERSION.INCREMENTAL}    \n" +

                "Build.VERSION.BASE_OS   ${Build.VERSION.BASE_OS}    \n" +
                "// 版本字符串    \n" +
                "Build.VERSION.RELEASE      ${Build.VERSION.RELEASE} \n" +
                "// 版本号    \n" +
                "Build.VERSION.SDK          ${Build.VERSION.SDK} \n" +

                "Build.VERSION.SECURITY_PATCH          ${Build.VERSION.SECURITY_PATCH} \n" +
                "// 版本号    \n" +
                "Build.VERSION.SDK_INT     ${Build.VERSION.SDK_INT}")
        LogUtils.e("   \n" +
                "Build.BOARD // 主板                    ${Build.BOARD}\n" +
                "Build.BRAND // android系统定制商        ${Build.BRAND}  \n" +
                "Build.CPU_ABI // cpu指令集              ${Build.CPU_ABI} \n" +
                "Build.DEVICE // 设备参数                ${Build.DEVICE} \n" +
                "Build.DISPLAY // 显示屏参数             ${Build.DISPLAY} \n" +
                "Build.FINGERPRINT // 硬件名称           ${Build.FINGERPRINT}  \n" +
                "Build.HOST                             ${Build.HOST} \n" +
                "Build.ID // 修订版本列表                ${Build.ID}\n" +
                "Build.MANUFACTURER // 硬件制造商        ${Build.MANUFACTURER}  \n" +
                "Build.MODEL // 版本                     ${Build.MODEL} \n" +
                "Build.PRODUCT // 手机制造商             ${Build.PRODUCT}     \n" +
                "Build.TAGS // 描述build的标签           ${Build.TAGS} \n" +
                "Build.TIME                             ${Build.TIME}  \n" +
                "Build.TYPE // builder类型               ${Build.TYPE}  \n" +
                "Build.USER                              ${Build.USER}")
    }

    override fun layoutId(): Int = R.layout.activity_setting


    override fun start() {

    }


}
