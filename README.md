# KotlinMvpDemo
在自我学习的过程中搭建的一个mvp 模式的demo

重新封装了retrofit+okhttp +rxjava 使用泛型处理统一的json 解析
根据回调接口的泛型类型进行返回处理， EasyRetrofit 处理头信息和response 的消息拦截进行统一的返回

 EasyRetrofit.instance.post(UrlConstant.weather_cast_3_days, map, object : ResponseCallBack<WeatherBean> {
            override fun onSuccess(response: WeatherBean) {
                view?.setHomeData(response)
            }

            override fun onError(throwable: Throwable) {
                view?.showError("加载失败",22)
            }

        })

添加了换肤框架，参考 Android-Skin-Loader ，再次基础上修改了加载方式，去除使用base类的方式，使用 ActivityLifecycleCallbacks生命回调接口进行注入拦截


使用方式大体上与Android-Skin-Loader大体一致 区别在于不需要在baseActivity里去加载 ，同时也增加了一些其他属性支持


首先将SkinManager 在application 里初始化


class MyAppLication : BaseApplication() {
       override fun onCreate() {
        super.onCreate()
        SkinManager.instance.init(this)
    }
}


在xml 开启 skin    xmlns:skin="http://schemas.android.com/android/skin"

 
在需要布局容器中添加    skin:enable="true" ，会自动扫描里面的 background color textcolor 等属性
 
 
 
加载皮肤


   SkinManager.instance.loadSkin(sdpath+ File.separator+"theme_w-debug.apk")
   
   
重置默认皮肤


   SkinManager.instance.reDefaultTheme()
