package com.wmy.kotlin.demo.http

/**
 *
 *@author：wmyasw
 */

import java.io.Serializable

/**
 * 标准数据格式
 * @author：wmyas
 * @date：2019/7/25 11:07
 */
class Response<T> : Serializable {
    var msg: String = ""
    var data: T? = null
    var code: Int = 0

    override fun toString(): String {
        return "Response{" +
                "msg='" + msg + '\''.toString() +
                ", data=" + data +
                ", code=" + code +
                '}'.toString()
    }
}