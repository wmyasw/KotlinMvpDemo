package com.wmy.kotlin.demo.ui

import android.app.Service
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.Message
import android.os.Vibrator
import com.wmy.kotlin.demo.R
import com.wmy.kotlin.demo.utils.LogUtils
import com.wmy.kotlin.mvp.lib.base.BaseActivity
import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract
import kotlinx.android.synthetic.main.activity_shake.*
import kotlinx.android.synthetic.main.content_shake.button
import kotlinx.android.synthetic.main.content_shake.textView2
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.util.*
import kotlin.collections.ArrayList

/**
 * 这是一个抽奖的小demo
 */
class ShakeActivity : BaseActivity<BaseContract.Presenter<Any>, Any>(), SensorEventListener {
    lateinit var list: MutableList<String>
    override fun initPresenter(): BaseContract.Presenter<Any>? {
        return null
    }

    //传感器管理类
    var sensorManager: SensorManager? = null
    //传感器
    var sensor: Sensor? = null
    //传感器
    var vibrator: Vibrator? = null

    override fun init() {

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = (sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER))
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        vibrator = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator?
        button.setOnClickListener {
            count = 1
            textView2.isEnabled = false
            randomText(count)
        }

    }

    override fun layoutId(): Int {
        return R.layout.activity_shake
    }
    //初始化数据
    override fun initData() {
//        setSupportActionBar(toolbar)
        list = ArrayList()
        list.add("胡雅楠")
        list.add("王明雨")
        list.add("刘超")
        list.add("苏智")
        list.add("罗禹")
        list.add("孙鹏")
        list.add("哈球球")
        list.add("任建红")
        list.add("郑卿吾")
        list.add("小萌老师")
        list.add("闫宇")
    }

    override fun start() {
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            var sensorType = event.sensor.type
            var values = event.values;
            // 如果传感器类型为加速度传感器，则判断是否为摇一摇
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                if ((Math.abs(values[0]) > 20 || Math.abs(values[1]) > 17 || Math.abs(values[2]) > 17)) {
                    if (count > maxTime) {//判断是否为重复晃动
                        count = 1
                        LogUtils.d("sensor x ", "============ values[0] = " + values[0])
                        LogUtils.d("sensor y ", "============ values[1] = " + values[1])
                        LogUtils.d("sensor z ", "============ values[2] = " + values[2])

                        randomText(count)
                    } else {
                        LogUtils.e("sensor", "2s 后再次允许摇动")
                    }
                }

            }

        }
    }


    var index: Int = 0 //记录随机人选下表
    var timer: Timer? = null //时间计时器
    var maxTime = 10 //设置最大时间 单位： s
    var count = maxTime+1
    /**
     * 创建随机事件展示
     */
    fun randomText(timestamp: Int) {
        //每次获取随机人员 的间隔 根据 最大随机时长比进行缩减  100毫秒*最长随机时间/5
        handler.postDelayed(runnable, (100 * timestamp / 5).toLong())
        if (timer == null) { //判断timer 为空时候创建一个计时器
            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    count++
                    LogUtils.e("时间控制 $count")
                }

            }, 0, 1000)
        }
    }

    //创建一个线程 发送给handler 消息队列
    var runnable = object : Runnable {
        override fun run() {
            var message = Message()
            message.what = 101
            handler.sendMessage(message)
        }

    }


    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 101) {
                if (count <= maxTime) { //判断当前执行时长是否小于最大可执行的循环时长
                    index = ((Math.random() * (list.size)).toInt())
                    textView2.setText(list.get(index))
                    randomText(count)
                } else {
                    timer?.cancel()
                    timer = null
                    textView2.setText("摇出幸运的同学:${list.get(index)}")
                    textView2.isEnabled = true
                    viewKonfetti.build() //天女散花
                            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                            .setDirection(0.0, 359.0)
                            .setSpeed(1f, 5f)
                            .setFadeOutEnabled(true)
                            .setTimeToLive(2000L)
                            .addShapes(Shape.RECT, Shape.CIRCLE)
                            .addSizes(Size(12))
                            .setPosition(-50f, viewKonfetti.width + 50f, -50f, -50f)
                            .streamFor(300, 5000L)
                    vibrator?.vibrate(500)
                }
            }
        }
    }

}
