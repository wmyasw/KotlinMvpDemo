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
