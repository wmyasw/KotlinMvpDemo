# KotlinMvpDemo
在自我学习的过程中搭建的一个mvp 模式的demo

修改基础类的处理逻辑，使用母版的形式，进行公共布局处理

    //逻辑处理 判断是否使用公共的toolbar，如果使用 则应用母版逻辑，如果不使用，则页面自行处理，基类不做干预
        if (isShowActionBar()) {
            setContentView(R.layout.activity_common)
            addSubView(layoutId())
            // TODO  可处理一些不同加载逻辑
          //  initActionBar()

        } else {
            setContentView(layoutId())
        }
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
   
  编写的一个自定义的悬浮框,参考微信的悬浮效果
  里面的item 添加暂未写
   https://github.com/wmyasw/KotlinMvpDemo/blob/master/floatwidget/Record_2019-12-31-14-46-54_8a1996bda865bd2d603615e0d14c159d-150925.gif
