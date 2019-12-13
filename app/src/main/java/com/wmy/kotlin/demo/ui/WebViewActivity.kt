package com.wmy.kotlin.demo.ui

import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.wmy.kotlin.demo.R
import com.wmy.kotlin.demo.utils.LogUtils
import com.wmy.kotlin.mvp.lib.base.BaseActivity
import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity: BaseActivity<BaseContract.Presenter<Any>, Any>(){



    override fun init() {




        // 从assets目录下面的加载html
        webView.loadUrl("file:///android_asset/test.html")
//        webView.loadUrl("https://www.baidu.com")
        webView.settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)
        webView.settings.setJavaScriptEnabled(true)
        webView.settings.setDomStorageEnabled(true)

        webView.addJavascriptInterface(this,"toast")
        webView.webViewClient=object:WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (view != null) {
                    setTitle(view.title)
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }


            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
//            return super.shouldOverrideUrlLoading(view, request)
                if (view != null) {
                    if (request != null) {

                        view.loadUrl(request.getUrl().toString())
                    }
                }
                return true

            }
        }
        webView.setWebChromeClient(object: WebChromeClient()
        {
         override fun onProgressChanged(view:WebView, progress:Int)
            {
                LogUtils.e("加载进度:$progress")
                //当进度走到100的时候做自己的操作，我这边是弹出dialog
                if(progress == 100){
//                    if (null != dialog &&!dialog.isShowing() && flag)
//                        dialog.show();
                }
            }
        })
    }
    @android.webkit.JavascriptInterface
    fun actionFromJs(){
        Toast.makeText(this,"wwwwwww",Toast.LENGTH_SHORT).show()
    }
    @android.webkit.JavascriptInterface
    fun actionFromJsWithParam(arg:String){
        Toast.makeText(this,arg,Toast.LENGTH_SHORT).show()
    }

    override fun layoutId(): Int {
      return  R.layout.activity_web_view
    }

    override fun initData() {
    }

    override fun start() {
    }

    open fun setToolbarTitle(title:String){
//        toolbar.setTitle(title)
//        setSupportActionBar(toolbar)
//        getSupportActionBar()?.setHomeButtonEnabled(true);
//        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar()?.setDisplayShowTitleEnabled(false);

    }
//    class MyWebViewClient :
//    }


}
