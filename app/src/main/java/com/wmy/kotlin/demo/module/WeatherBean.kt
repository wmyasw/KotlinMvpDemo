package com.wmy.kotlin.demo.module

import java.io.Serializable

/**
 * 定义一个天气的bean
 *@author：wmyasw
 */
class WeatherBean :Serializable{

    /**
     * city : {"cityId":284609,"counname":"中国","ianatimezone":"Asia/Shanghai","name":"东城区","pname":"北京市","secondaryname":"北京市","timezone":"8"}
     * forecast : [{"conditionDay":"小雪","conditionIdDay":"14","conditionIdNight":"14","conditionNight":"小雪","humidity":"46","predictDate":"2019-11-29","tempDay":"2","tempNight":"-4","updatetime":"2019-11-29 14:06:00","windDegreesDay":"180","windDegreesNight":"225","windDirDay":"南风","windDirNight":"西南风","windLevelDay":"3","windLevelNight":"3"},{"conditionDay":"多云","conditionIdDay":"1","conditionIdNight":"30","conditionNight":"晴","humidity":"54","predictDate":"2019-11-30","tempDay":"6","tempNight":"-5","updatetime":"2019-11-29 14:06:00","windDegreesDay":"315","windDegreesNight":"0","windDirDay":"西北风","windDirNight":"北风","windLevelDay":"3-4","windLevelNight":"3"},{"conditionDay":"晴","conditionIdDay":"0","conditionIdNight":"30","conditionNight":"晴","humidity":"26","predictDate":"2019-12-01","tempDay":"4","tempNight":"-4","updatetime":"2019-11-29 14:06:00","windDegreesDay":"315","windDegreesNight":"315","windDirDay":"西北风","windDirNight":"西北风","windLevelDay":"3-4","windLevelNight":"3-4"}]
     */

    private var city: CityBean? = null
    private var forecast: List<ForecastBean>? = null

    fun getCity(): CityBean? {
        return city
    }

    fun setCity(city: CityBean) {
        this.city = city
    }

    fun getForecast(): List<ForecastBean>? {
        return forecast
    }

    fun setForecast(forecast: List<ForecastBean>) {
        this.forecast = forecast
    }

    class CityBean:Serializable {
        /**
         * cityId : 284609
         * counname : 中国
         * ianatimezone : Asia/Shanghai
         * name : 东城区
         * pname : 北京市
         * secondaryname : 北京市
         * timezone : 8
         */

        var cityId: Int = 0
        var counname: String? = null
        var ianatimezone: String? = null
        var name: String? = null
        var pname: String? = null
        var secondaryname: String? = null
        var timezone: String? = null
    }

    class ForecastBean :Serializable{
        /**
         * conditionDay : 小雪
         * conditionIdDay : 14
         * conditionIdNight : 14
         * conditionNight : 小雪
         * humidity : 46
         * predictDate : 2019-11-29
         * tempDay : 2
         * tempNight : -4
         * updatetime : 2019-11-29 14:06:00
         * windDegreesDay : 180
         * windDegreesNight : 225
         * windDirDay : 南风
         * windDirNight : 西南风
         * windLevelDay : 3
         * windLevelNight : 3
         * conditionDay	白天天气
         * conditionIdDay	白天天气id
         * conditionNight	夜间天气
         * conditionIdNight	夜间天气id
         * humidity	湿度	predictDate	预报日期	tempDay	白天温度	tempNight	夜间温度	updatetime	更新时间	windDegreesDay	白天风向角度	windDegreesNight	夜间风向角度	windDirDay	白天风向	windDirNight	夜间风向
         */
        var conditionDay: String? = null
        var conditionIdDay: String? = null
        var conditionIdNight: String? = null
        var conditionNight: String? = null
        var humidity: String? = null
        var predictDate: String? = null
        var tempDay: String? = null
        var tempNight: String? = null
        var updatetime: String? = null
        var windDegreesDay: String? = null
        var windDegreesNight: String? = null
        var windDirDay: String? = null
        var windDirNight: String? = null
        var windLevelDay: String? = null
        var windLevelNight: String? = null
    }
}