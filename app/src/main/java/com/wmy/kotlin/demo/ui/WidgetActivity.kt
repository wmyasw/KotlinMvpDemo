package com.wmy.kotlin.demo.ui

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wmy.kotlin.demo.R
import com.wmy.kotlin.demo.utils.LogUtils
import com.wmy.kotlin.mvp.lib.base.BaseActivity
import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract.Presenter
import kotlinx.android.synthetic.main.activity_widget.*

class WidgetActivity : BaseActivity<Presenter<Any>, Any>() {
    override fun initPresenter(): Presenter<Any>? {
        return null
    }
    var sensorManager: SensorManager? =null
    var listener: SensorEventListener? =null
    override fun init() {


// 取传感器
         sensorManager =
                getSystemService(SENSOR_SERVICE) as SensorManager;
        // 获取陀螺仪
        val  gyroscopeSensor =
                sensorManager!!.getDefaultSensor(Sensor.TYPE_ORIENTATION)

        listener=object :SensorEventListener{
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }

            override fun onSensorChanged(p0: SensorEvent?) {
                var values= p0!!.values

                var value0=values[0];
                var value1=values[1];
                LogUtils.e("打印数据：$value0  $value1")
//                compassView.setValues(values)
            }

        }
// Create a listener

        sensorManager!!.registerListener(listener,
                gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    override fun layoutId(): Int =R.layout.activity_widget

    override fun initData() {
    }

    override fun start() {
    }


    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(listener)

    }
}
