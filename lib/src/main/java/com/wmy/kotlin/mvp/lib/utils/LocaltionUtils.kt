
import android.Manifest
import android.os.Bundle
import android.content.pm.PackageManager
import android.Manifest.permission
import androidx.core.app.ActivityCompat
import android.os.Build


import android.content.ContentValues.TAG
import android.content.Context
import android.location.*
import android.util.Log
import com.wmy.kotlin.demo.utils.LogUtils

class LocationUtils private constructor(private val mContext: Context) {

    private var locationManager: LocationManager? = null
    private var locationProvider: String? = null
    private var location: Location? = null

    /**
     * LocationListern监听器
     * 参数：地理位置提供器、监听位置变化的时间间隔、位置变化的距离间隔、LocationListener监听器
     */

    internal var locationListener: LocationListener = object : LocationListener {

        /**
         * 当某个位置提供者的状态发生改变时
         */
        override fun onStatusChanged(provider: String, status: Int, arg2: Bundle) {

        }

        /**
         * 某个设备打开时
         */
        override fun onProviderEnabled(provider: String) {

        }

        /**
         * 某个设备关闭时
         */
        override fun onProviderDisabled(provider: String) {

        }

        /**
         * 手机位置发生变动
         */
        override fun onLocationChanged(location: Location) {
            location.getAccuracy()//精确度
            setLocation(location)
        }
    }

    init {
        getLocation()
    }

    private fun getLocation() {
        //1.获取位置管理器
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        //2.获取位置提供器，GPS或是NetWork
        val providers = locationManager!!.getProviders(true)
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
          LogUtils.e( "如果是网络定位")
            locationProvider = LocationManager.NETWORK_PROVIDER
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            LogUtils.e( "如果是GPS定位")
            locationProvider = LocationManager.GPS_PROVIDER
        } else {
            LogUtils.e( "没有可用的位置提供器")
            return
        }
        // 需要检查权限,否则编译报错,想抽取成方法都不行,还是会报错。只能这样重复 code 了。
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogUtils.e( "没有可用的位置提供器")
            return
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogUtils.e( "没有可用的位置提供器")
            return
        }
        //3.获取上次的位置，一般第一次运行，此值为null
        val location = locationManager!!.getLastKnownLocation(locationProvider!!)
        if (location != null) {
            setLocation(location)
        }
        // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
        locationManager!!.requestLocationUpdates(locationProvider!!, 0, 0f, locationListener)
    }

    private fun setLocation(location: Location) {
        this.location = location
        val address = "纬度：" + location.getLatitude() + "经度：" + location.getLongitude()
        Log.d(TAG, address)
    }

    //获取经纬度
    fun showLocation(): Location? {
        return location
    }

    // 移除定位监听
    fun removeLocationUpdatesListener() {
        // 需要检查权限,否则编译不过
        if (Build.VERSION.SDK_INT >= 23 &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        if (locationManager != null) {
            uniqueInstance = null
            locationManager!!.removeUpdates(locationListener)
        }
    }

    companion object {

        @Volatile
        private var uniqueInstance: LocationUtils? = null

        //采用Double CheckLock(DCL)实现单例
        fun getInstance(context: Context): LocationUtils? {
            if (uniqueInstance == null) {
                synchronized(LocationUtils::class.java) {
                    if (uniqueInstance == null) {
                        uniqueInstance = LocationUtils(context)
                    }
                }
            }
            return uniqueInstance
        }
    }

}
