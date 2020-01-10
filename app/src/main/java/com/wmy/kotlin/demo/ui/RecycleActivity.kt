package com.wmy.kotlin.demo.ui

import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.wmy.kotlin.demo.R
import com.wmy.kotlin.demo.adpter.RvAdapter
import com.wmy.kotlin.demo.module.WeatherBean
import com.wmy.kotlin.demo.mvp.RecycleContract
import com.wmy.kotlin.demo.mvp.RecyclePresenter
import com.wmy.kotlin.mvp.lib.base.BaseActivity
import kotlinx.android.synthetic.main.activity_recycle.*
class RecycleActivity : BaseActivity<RecycleContract.Presenter<RecycleContract.View>, RecycleContract.View>(), RecycleContract.View{
    lateinit var adapter: RvAdapter
    var list = mutableListOf<WeatherBean>()

    override fun initPresenter(): RecycleContract.Presenter<RecycleContract.View>? {
        return RecyclePresenter(this)
    }

    override fun init() {
        rv_list.layoutManager= LinearLayoutManager(this)
    }

    override fun layoutId(): Int= R.layout.activity_recycle



    override fun start() {
        mPresenter?.requestHomeData()
    }

    override fun setHomeData(w: MutableList<String>) {
        adapter=RvAdapter(this,w)
        adapter.setListener(object :RvAdapter.MyListener{
            override fun itemListener(v: View?, position: Int) {
               var text_view = v?.findViewById(R.id.textView13) as TextView
               var imageView2 = v?.findViewById(R.id.imageView2) as ImageView
                startActivity2(RecycleDetailActivity::class.java,imageView2,text_view)
            }

        })
        rv_list.adapter=adapter

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun <T> startActivity2(clazz: Class<T>, img:View, text:View) {
        val intent = Intent(this,clazz)

        val imageTransition = androidx.core.util.Pair<View, String>(img, img.transitionName);
        val textTransition = androidx.core.util.Pair<View, String>(text, text.transitionName);
        val compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@RecycleActivity, *arrayOf(imageTransition, textTransition))
        ActivityCompat.startActivity(this,intent, compat.toBundle())
    }
    override fun setMoreData() {

    }

    override fun showError(msg: String, errorCode: Int) {

    }

    override fun showError(message: String?) {

    }

    override val isNetworkConnected: Boolean=true


    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
