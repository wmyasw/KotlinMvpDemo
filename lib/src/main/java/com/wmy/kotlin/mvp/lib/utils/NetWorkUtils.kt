package com.wmy.kotlin.demo.utils

/**
 *
 *@author：wmyasw
 */

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager

import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.UnknownHostException
import java.util.Enumeration
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * 网络相关工具类
 */
class NetworkUtils private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    enum class NetworkType {
        NETWORK_WIFI,
        NETWORK_4G,
        NETWORK_3G,
        NETWORK_2G,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    companion object {

        /**
         * 打开网络设置界面
         *
         * 3.0以下打开设置界面
         */
        fun openWirelessSettings() {
            if (android.os.Build.VERSION.SDK_INT > 10) {
                Utils.getContext().startActivity(Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            } else {
                Utils.getContext().startActivity(Intent(android.provider.Settings.ACTION_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }

        /**
         * 获取活动网络信息
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
         *
         * @return NetworkInfo
         */
        private val activeNetworkInfo: NetworkInfo?
            get() =
                (Utils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

        /**
         * 判断网络是否连接
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        val isConnected: Boolean
            get() {
                val info = activeNetworkInfo
                return info != null && info.isConnected
            }

        /**
         * 判断网络是否可用
         *
         * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
         *
         * @return `true`: 可用<br></br>`false`: 不可用
         */
        val isAvailableByPing: Boolean
            get() {
                val result = ShellUtils.execCmd("ping -c 1 -w 1 223.5.5.5", false)
                val ret = result.result === 0
                if (result.errorMsg != null) {
                    LogUtils.d("isAvailableByPing errorMsg", result.errorMsg)
                }
                if (result.successMsg != null) {
                    LogUtils.d("isAvailableByPing successMsg", result.successMsg)
                }
                return ret
            }

        /**
         * 判断移动数据是否打开
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        /**
         * 打开或关闭移动数据
         *
         * 需系统应用 需添加权限`<uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/>`
         *
         * @param enabled `true`: 打开<br></br>`false`: 关闭
         */
        var dataEnabled: Boolean
            get() {
                try {
                    val tm = Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    val getMobileDataEnabledMethod = tm.javaClass.getDeclaredMethod("getDataEnabled")
                    if (null != getMobileDataEnabledMethod) {
                        return getMobileDataEnabledMethod.invoke(tm) as Boolean
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return false
            }
            set(enabled) = try {
                val tm = Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val setMobileDataEnabledMethod = tm.javaClass.getDeclaredMethod("setDataEnabled", Boolean::class.javaPrimitiveType)
                if (null == setMobileDataEnabledMethod) {

                } else {
                    setMobileDataEnabledMethod.invoke(tm, enabled) as Unit
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        /**
         * 判断网络是否是4G
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        val is4G: Boolean
            get() {
                val info = activeNetworkInfo
                return info != null && info.isAvailable && info.subtype == TelephonyManager.NETWORK_TYPE_LTE
            }

        /**
         * 判断wifi是否打开
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>`
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        /**
         * 打开或关闭wifi
         *
         * 需添加权限 `<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>`
         *
         * @param enabled `true`: 打开<br></br>`false`: 关闭
         */
        var wifiEnabled: Boolean
            get() {
                val wifiManager = Utils.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                return wifiManager.isWifiEnabled
            }
            set(enabled) {
                val wifiManager = Utils.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                if (enabled) {
                    if (!wifiManager.isWifiEnabled) {
                        wifiManager.isWifiEnabled = true
                    }
                } else {
                    if (wifiManager.isWifiEnabled) {
                        wifiManager.isWifiEnabled = false
                    }
                }
            }

        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

        /**
         * 判断wifi是否连接状态
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
         *
         * @return `true`: 连接<br></br>`false`: 未连接
         */
        val isWifiConnected: Boolean
            get() {
                val cm = Utils.getContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                return (cm != null && cm.activeNetworkInfo != null
                        && cm.activeNetworkInfo!!.type == ConnectivityManager.TYPE_WIFI)
            }

        /**
         * 判断wifi数据是否可用
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>`
         *
         * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
         *
         * @return `true`: 是<br></br>`false`: 否
         */
        val isWifiAvailable: Boolean
            get() = wifiEnabled && isAvailableByPing

        /**
         * 获取网络运营商名称
         *
         * 中国移动、如中国联通、中国电信
         *
         * @return 运营商名称
         */
        val networkOperatorName: String?
            get() {
                val tm = Utils.getContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                return tm?.networkOperatorName
            }

        private val NETWORK_TYPE_GSM = 16
        private val NETWORK_TYPE_TD_SCDMA = 17
        private val NETWORK_TYPE_IWLAN = 18

        /**
         * 获取当前网络类型
         *
         * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
         *
         * @return 网络类型
         *
         *  * [NetworkUtils.NetworkType.NETWORK_WIFI]
         *  * [NetworkUtils.NetworkType.NETWORK_4G]
         *  * [NetworkUtils.NetworkType.NETWORK_3G]
         *  * [NetworkUtils.NetworkType.NETWORK_2G]
         *  * [NetworkUtils.NetworkType.NETWORK_UNKNOWN]
         *  * [NetworkUtils.NetworkType.NETWORK_NO]
         *
         */
        val networkType: NetworkType
            get() {
                var netType = NetworkType.NETWORK_NO
                val info = activeNetworkInfo
                if (info != null && info.isAvailable) {

                    if (info.type == ConnectivityManager.TYPE_WIFI) {
                        netType = NetworkType.NETWORK_WIFI
                    } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
                        when (info.subtype) {

                            NETWORK_TYPE_GSM, TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> netType = NetworkType.NETWORK_2G

                            NETWORK_TYPE_TD_SCDMA, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> netType = NetworkType.NETWORK_3G

                            NETWORK_TYPE_IWLAN, TelephonyManager.NETWORK_TYPE_LTE -> netType = NetworkType.NETWORK_4G
                            else -> {

                                val subtypeName = info.subtypeName
                                if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                                        || subtypeName.equals("WCDMA", ignoreCase = true)
                                        || subtypeName.equals("CDMA2000", ignoreCase = true)) {
                                    netType = NetworkType.NETWORK_3G
                                } else {
                                    netType = NetworkType.NETWORK_UNKNOWN
                                }
                            }
                        }
                    } else {
                        netType = NetworkType.NETWORK_UNKNOWN
                    }
                }
                return netType
            }

        /**
         * 获取IP地址
         *
         * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
         *
         * @param useIPv4 是否用IPv4
         * @return IP地址
         */
        fun getIPAddress(useIPv4: Boolean): String? {
            try {
                val nis = NetworkInterface.getNetworkInterfaces()
                while (nis.hasMoreElements()) {
                    val ni = nis.nextElement()
                    // 防止小米手机返回10.0.2.15
                    if (!ni.isUp) continue
                    val addresses = ni.inetAddresses
                    while (addresses.hasMoreElements()) {
                        val inetAddress = addresses.nextElement()
                        if (!inetAddress.isLoopbackAddress) {
                            val hostAddress = inetAddress.hostAddress
                            val isIPv4 = hostAddress.indexOf(':') < 0
                            if (useIPv4) {
                                if (isIPv4) return hostAddress
                            } else {
                                if (!isIPv4) {
                                    val index = hostAddress.indexOf('%')
                                    return if (index < 0) hostAddress.toUpperCase() else hostAddress.substring(0, index).toUpperCase()
                                }
                            }
                        }
                    }
                }
            } catch (e: SocketException) {
                e.printStackTrace()
            }

            return null
        }

        /**
         * 获取域名ip地址
         *
         * 需添加权限 `<uses-permission android:name="android.permission.INTERNET"/>`
         *
         * @param domain 域名
         * @return ip地址
         */
        fun getDomainAddress(domain: String): String? {
            try {
                val exec = Executors.newCachedThreadPool()
                val fs = exec.submit(Callable<String> {
                    val inetAddress: InetAddress
                    try {
                        inetAddress = InetAddress.getByName(domain)
                        return@Callable inetAddress.hostAddress
                    } catch (e: UnknownHostException) {
                        e.printStackTrace()
                    }

                    null
                })
                return fs.get()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }

            return null
        }
    }
}