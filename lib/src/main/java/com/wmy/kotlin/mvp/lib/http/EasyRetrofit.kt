package com.wmy.kotlin.demo.http

import com.google.gson.Gson
import com.hazz.kotlinmvp.api.UrlConstant
import com.hazz.kotlinmvp.utils.AppUtils
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.wmy.kotlin.demo.constant.ApiConstant
import com.wmy.kotlin.demo.utils.LogUtils
import com.wmy.kotlin.demo.utils.NetworkUtils
import com.wmy.kotlin.mvp.lib.base.BaseApplication
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.reflect.ParameterizedType
import java.util.concurrent.TimeUnit

/**
 * 一个简单的通用http 管理类
 * 封装通用的 post get 方法 ，通过回调接口泛型解析获取返回对象类型
 * 通过返回对象拦截 处理统一的数据回调
 *@author：wmyasw
 */
class EasyRetrofit{

    private val retrofit: Retrofit
    private val service: ApiService

    companion object {
        val instance by lazy {
            val easyRetorfit: EasyRetrofit = EasyRetrofit()
            easyRetorfit
        }
    }

    init {
        retrofit = getRetrofit()
        service = retrofit.create(ApiService::class.java)
    }

    /**
     * 设置公共参数
     */
    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
                    // Provide your custom parameter here
                    .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
                    .addQueryParameter("deviceModel", AppUtils.getMobileModel())
                    .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    /**
     * 设置头
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                    // Provide your custom header here
                    .header("token", "")
                    .header("Authorization", "APPCODE ${ApiConstant.AppCode}")
                    .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    /**
     * 设置缓存
     */
    private fun addCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!NetworkUtils.isNetworkAvailable(BaseApplication.ins?.applicationContext)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }
            val response = chain.proceed(request)
            if (NetworkUtils.isNetworkAvailable(BaseApplication.ins?.applicationContext)) {
                val maxAge = 0
                // 有网络时 设置缓存超时时间0个小时 ,意思就是不读取缓存数据,只对get有用,post没有缓冲
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .removeHeader("Retrofit")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .build()
            } else {
                // 无网络时，设置超时为4周  只对get有用,post没有缓冲
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("nyn")
                        .build()
            }
            response
        }
    }

    /**
     * 初始化retrofit
     */
    private fun getRetrofit(): Retrofit {
        // 获取retrofit的实例
        return Retrofit.Builder()
                .baseUrl(UrlConstant.BASE_URL)  //自己配置
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }

    /**
     * 初始化OkhttpClient
     */
    private fun getOkHttpClient(): OkHttpClient {
        //添加一个log拦截器,打印所有的log
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        //可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        //设置 请求的缓存的大小跟位置
        val cacheFile = File(BaseApplication.ins?.applicationContext.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50) //50Mb 缓存的大小

        return OkHttpClient.Builder()
                .addInterceptor(addQueryParameterInterceptor())  //参数添加
                .addInterceptor(addHeaderInterceptor()) // token过滤
//              .addInterceptor(addCacheInterceptor())
                .addInterceptor(httpLoggingInterceptor) //日志,所有的请求响应度看到
                .cache(cache)  //添加缓存
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .build()
    }


    /**
     * 公用的get 请求
     *
     * @param url              请求地址
     * @param maps             请求参数
     * @param responseCallBack 回调监听
     */
    operator fun <T> get(url: String, maps: Map<String, Any>, listener: ResponseCallBack<T>) {
        var map: Map<*, *>? = maps
        service.get(url, map as Map<String, String>)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener))
//
    }


    /**
     * 公用的post 请求
     *
     * @param url              请求地址
     * @param maps             请求参数
     * @param responseCallBack 回调监听
     */
    fun <T> post(url: String, maps: Map<String, Any>, listener: ResponseCallBack<T>) {
        var map: Map<*, *>? = maps
        service.post(url, map as Map<String, String>)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener))
    }


    /**
     * 正常请求 订阅监听及拦截
     *
     * @param listener
     * @return
     */
    private fun getObserver(listener: ResponseCallBack<*>): Observer<Response<Any>> {
        return object : Observer<Response<Any>> {
            override fun onError(e: Throwable) {
                LogUtils.d("onError")
                e.printStackTrace()
                if (listener != null) {
                    listener!!.onError(e)
                }
            }

            /**
             * Notifies the Observer that the [Observable] has finished sending push-based notifications.
             *
             *
             * The [Observable] will not call this method if it calls [.onError].
             */
            override fun onComplete() {

            }

            /**
             * Provides the Observer with the means of cancelling (disposing) the
             * connection (channel) with the Observable in both
             * synchronous (from within [.onNext]) and asynchronous manner.
             *
             * @param d the Disposable instance whose [Disposable.dispose] can
             * be called anytime to cancel the connection
             * @since 2.0
             */
            override fun onSubscribe(d: Disposable) {
                LogUtils.d("onSubscribe")
            }

            /**
             * Provides the Observer with a new item to observe.
             *
             *
             * The [Observable] may call this method 0 or more times.
             *
             *
             * The `Observable` will not call this method again after it calls either [.onComplete] or
             * [.onError].
             *
             * @param response the item emitted by the Observable
             */
            override fun onNext(response: Response<Any>) {
                LogUtils.d("onNext :-----------")
                //处理监听为空的逻辑
                if (listener == null) return
//                if (response.code !== 200) {
//                    onError(Throwable("业务错误"))
//                    return
//                }
                //这里获得到的是类的泛型的类型
                LogUtils.d("onNext rawType length ：" + listener.javaClass.genericInterfaces.size)
                LogUtils.d("onNext rawType ：" + listener!!.javaClass.genericInterfaces[0])


                try {
                    converJson(response, listener)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onError(Throwable("转换异常 ：", e))
                }

            }

        }
    }

    /**
     * 转换返回对象 根据泛型类型处理转换
     * @param response
     * @param listener
     */
    private fun <T> converJson(response: Response<Any>, listener: ResponseCallBack<T>) {
        var myJson = ""
        if (null != response.data) {
            myJson = Gson().toJson(response.data)
        }
        if (listener.javaClass.genericInterfaces.isNotEmpty() && listener.javaClass.genericInterfaces.size > 0) {
            val type = listener!!.javaClass.genericInterfaces[0]
            // 泛型的实际类型
            val typeArgument = (type as ParameterizedType).actualTypeArguments[0] // 泛型的参数
            LogUtils.d("onNext typeArgument ：$typeArgument")
            LogUtils.d("onNext myJson ：$myJson")
//            val superType=Response<Any>().data
            listener!!.onSuccess(Gson().fromJson<Any>(myJson, typeArgument) as T)

        } else {
            listener!!.onSuccess(myJson as T)
        }
    }


}




