package com.wmy.kotlin.demo.http

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 *
 *@author：wmyasw
 */
interface ApiService {

    @GET
    @JvmSuppressWildcards
    fun  get(@Url url: String, @QueryMap maps: Map<String, String>): Observable<Response<Any>>

    @FormUrlEncoded
    @POST
    @JvmSuppressWildcards
    fun post(@Url url: String, @FieldMap maps: Map<String, String>): Observable<Response<Any>>
    fun postString(@Url url: String, @FieldMap maps: Map<String, String>): Observable<*>

    @POST
    fun postBody(@Url url: String, @QueryMap urlMaps: Map<String, String>, @Body body: ResponseBody): Observable<Response<*>>
}