package com.wmy.kotlin.demo.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.wmy.kotlin.demo.R
import com.wmy.kotlin.demo.utils.LogUtils
import com.wmy.kotlin.mvp.lib.base.BaseActivity
import com.wmy.kotlin.mvp.lib.mvp.contract.BaseContract
import kotlinx.android.synthetic.main.activity_bluetooth.*


class BluetoothActivity : BaseActivity<BaseContract.Presenter<Any>, Any>() {
    private var REQUEST_ENABLE_BT = 11
    lateinit var filter: IntentFilter
    var mBluetoothAdapter:BluetoothAdapter?=null
    override fun init() {

        filter = IntentFilter(BluetoothDevice.ACTION_FOUND)

         mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter!!.isEnabled()) {
                var enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }else{
                registerReceiver(mReceiver, filter)
            }
        }

    }

    override fun layoutId(): Int = R.layout.activity_bluetooth

    override fun initData() {
        button2.setOnClickListener { //如果当前在搜索，就先取消搜索
            if (mBluetoothAdapter?.isDiscovering()!!) {
                mBluetoothAdapter?.cancelDiscovery()
            }
            mBluetoothAdapter!!.startDiscovery()
        }
    }

    override fun start() {


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        LogUtils.e("requestCode ：$requestCode")
        if(requestCode==REQUEST_ENABLE_BT){
            registerReceiver(mReceiver, filter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

    /**
     * 发现蓝牙的广播
     */
    var mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        override fun onReceive(p0: Context?, p1: Intent?) {
            var action = p1?.getAction()
            LogUtils.e("BroadcastReceiver  $action")

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                var device: BluetoothDevice = p1?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
                LogUtils.e("BroadcastReceiver  ${device.name} ${device.uuids} ${device.address}  ${device.type} ${device.bluetoothClass} ${device.bondState}")
                //todo处理搜索到的蓝牙设备
            }
        }

    }


}
