package com.wmy.kotlin.demo.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import com.google.android.material.navigation.NavigationView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.wmy.kotlin.demo.R
import com.wmy.kotlin.demo.module.WeatherBean
import com.wmy.kotlin.demo.mvp.HomeContract
import com.wmy.kotlin.demo.mvp.MainPresenter
import com.wmy.kotlin.demo.utils.NetworkUtils
import com.wmy.kotlin.mvp.lib.base.BaseActivity
import com.wmy.kotlin.mvvm.floatwidget.FloatManager
import com.wmy.kotlin.mvvm.theme.SkinManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity : BaseActivity<HomeContract.Presenter<HomeContract.View>, HomeContract.View>(), HomeContract.View {


    override fun init() {
        var rxPermission = RxPermissions(this)
        rxPermission.requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS)
                .subscribe({ permission ->
                    if (permission.granted) {
                        getWeatherData()
                    } else if (permission.shouldShowRequestPermissionRationale) {
//                        Log.e(FragmentActivity.TAG, permission.shouldShowRequestPermissionRationale + "")
                    } else {
//                        Log.e(FragmentActivity.TAG, permission.name + "权限被禁止！")
                    }
                })
        mPresenter!!.attachView(this as HomeContract.View)
        nav_view.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(p0: MenuItem): Boolean {
                if (p0.itemId == R.id.nav_home) startActivity1(WebViewActivity::class.java)
                if (p0.itemId == R.id.nav_tools) startActivity1(SettingActivity::class.java)
                if (p0.itemId == R.id.nav_slideshow) startActivity1(BluetoothActivity::class.java)
                if (p0.itemId == R.id.nav_gallery) startActivity1(ZYActivity::class.java)
                if (p0.itemId == R.id.nav_send) startActivity1(RecycleActivity::class.java)
                return true
            }

        })


    }


    override fun layoutId(): Int = R.layout.activity_main2
    override fun isShowActionBar(): Boolean = false


     fun initData() {
//        setTitle("首页")
//        toolbar.title = "首页"
//        setSupportActionBar(toolbar)
        setData(null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            FloatManager.instance!!.showWindow(this)
        }
//        FloatManager.instance!!.setViewMove(true)
    }


    fun getWeatherData() {
        var location = LocationUtils.getInstance(this)!!.showLocation() as Location
        mPresenter?.requestHomeData(location)
    }

    fun setData(data: WeatherBean?) {
//
//            }     panel_city_text.setText(data.getCity()?.name)
////        panel_temp_now_text.setText(data.getForecast()!!.get(0).tempDay)
////        panel_weather_desc_text.setText(data.getForecast()!!.get(0).conditionDay)
////        panel_air_condition_text.setText("空气 " + data.getForecast()!!.get(0).conditionDay)
//
////        layout1.setOnClickListener(object : OnClickListener {
////            override fun onClick(p0: View?) {
////                get()
//        })


        layout1.setOnClickListener(object : OnClickListener {
            override fun onClick(p0: View?) {
//                get()
                startActivity1(WidgetActivity::class.java)
//                val sdpath: String = Environment.getExternalStorageDirectory().getAbsolutePath()
//                SkinManager.instance.loadSkin(sdpath + File.separator + "theme_w-debug.apk")
            }
        })
        layout2.setOnClickListener(object : OnClickListener {
            override fun onClick(p0: View?) {
                SkinManager.instance.reDefaultTheme()
            }

        })
        layout3.setOnClickListener(object : OnClickListener {
            override fun onClick(p0: View?) {
                startActivity1(ShakeActivity::class.java)
            }

        })
    }

    override fun start() {
//        mPresenter.start()
    }


    /**
     * 测试微信调取小程序
     */
    fun get() {
        val appId = "wxf0c2572a568dbd6b" // 填应用AppId

        val api = WXAPIFactory.createWXAPI(this, appId)
        api.registerApp(appId)

        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) { // 将该app注册到微信
                api.registerApp(appId)
            }
        }, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP))

        val req = WXLaunchMiniProgram.Req()
        req.userName = "gh_faf6a9b7028e" // 填小程序原始id

//        req.path = path ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。

        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_TEST // 可选打开 开发版，体验版和正式版

        api.sendReq(req)
    }


    override fun initPresenter(): HomeContract.Presenter<HomeContract.View> {
        return MainPresenter(this)
    }

    override val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkAvailable(this) //To change initializer of created properties use File | Settings | File Templates.

    override fun setHomeData(w: WeatherBean) {
        setData(w)
    }

    override fun setMoreData() {
    }

    override fun showError(msg: String, errorCode: Int) {
    }

    override fun showError(message: String?) {
    }


}

