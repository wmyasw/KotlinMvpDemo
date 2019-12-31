package com.wmy.kotlin.mvp.lib.base;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wmy.kotlin.mvp.lib.R
import com.wmy.kotlin.mvp.lib.utils.FileController
import java.sql.Timestamp

class LogFragment : Fragment() {
    var loggerText: ScrollView? = null
    var logfield: LinearLayout? = null
    var LOG_TEXT_ACTION="com.intent.logs"
    var logReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            var text = p1!!.getStringExtra("logs")
            logTextToScroll(text)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater.inflate(R.layout.fragment_log, container, false)
        loggerText = v.findViewById(R.id.log);
        logfield = v.findViewById(R.id.linearLayout1)

        FileController.fileControl!!.readFromLogFile()?.let { logTextToScroll(it) }
        return v
//         return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
        var intentFilter = IntentFilter()


        intentFilter = IntentFilter()
        intentFilter.addAction(LOG_TEXT_ACTION)
        activity!!.registerReceiver(logReceiver, intentFilter)
    }


    ////注册广播
//    @Override
//    public void onStart() {
//        super.onStart();
//        IntentFilter intentFilter = new IntentFilter();
//
//        logReceiver = new LogReceiver();
//        intentFilter = new IntentFilter();
//        intentFilter.addAction(LOG_TEXT_ACTION);
//        getActivity().registerReceiver(logReceiver, intentFilter);
//    }


    @Override
    override fun onResume()
    {
        super.onResume();
        loggerText!!.post(object: Runnable {

            override fun run() {
                loggerText!!.fullScroll(View.FOCUS_DOWN)
            }
        })
    }

//注销receiver

    override fun onStop()
    {
        super.onStop()
        getActivity()!!.unregisterReceiver(logReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        FileController.fileControl!!.deleteLogFile()
    }


    //将带有log的textview 添加进布局中
    fun logTextToScroll(str:String) {
        if (str == null || str.isEmpty())
            return

        var t = TextView(getContext())
        var s =str.plus("\n")
        t.setText(s)
        logfield!!.addView(t)
    }

    //清除界面的log，可以在界面添加button 或者菜单，通过点击实现清除log
    fun clearLog() {
        for (i in 0 until logfield!!.childCount) {
            var t = logfield!!.getChildAt(i) as TextView
            t.setText("")
            logfield!!.removeAllViews()
        }
    }


}