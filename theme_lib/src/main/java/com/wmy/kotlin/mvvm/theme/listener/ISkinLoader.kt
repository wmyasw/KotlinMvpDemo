package com.wmy.kotlin.mvvm.theme.listener

/**
 *
 *@author：wmyasw
 */
interface ISkinLoader {
    fun attach(observer: ISkinUpdate?)
    fun detach(observer: ISkinUpdate?)
    fun notifySkinUpdate()
}