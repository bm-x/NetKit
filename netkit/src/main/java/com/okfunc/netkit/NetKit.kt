package com.okfunc.netkit

import com.okfunc.netkit.core.NetkitConfig
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MultipartBody

//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.app.Application
//import android.support.v4.app.Fragment
//import okhttp3.OkHttpClient
//
//
/**
 *
 * Created by buck on 2017/10/28.
 */

val MEDIA_TYPE_PLAIN get() = MediaType.parse("text/plain;charset=utf-8")
val MEDIA_TYPE_JSON get() = MediaType.parse("application/json;charset=utf-8")
val MEDIA_TYPE_STREAM get() = MediaType.parse("application/octet-stream")

val MEDIA_TYPE_FORM get() = MultipartBody.FORM

val MEDIA_TYPE_FORM_URLENCODED = MediaType.get("application/x-www-form-urlencoded")

object NetKit {

    var globalConfig: NetkitConfig? = null

//
//    var globalConfig: NkConfig = NkConfig()
//        set(value) {
//            if (value.application == null) value.application = field.application
//            field = value
//        }
//
//    var okHttpClient: OkHttpClient? = null
//
//    fun cancel(tag: Any) = NkController.cancel(tag)
//
//    fun cancelAll() = NkController.cancelAll()
//
//    fun request(path: String?) = NetKitUrl(null, path)
//
//    fun request(host: String?, path: String?) = NetKitUrl(null, null)
//
//    fun get(path: String?) = NetKitGetUrl(path)
//
//    fun get(host: String?, path: String?) = NetKitGetUrl(host, path)
//
//    fun post(path: String?) = NetKitPostUrl(path)
//
//    fun post(host: String?, path: String?) = NetKitPostUrl(host, path)
//
//    fun application(app: Application) {
//        globalConfig.application = app
//    }
//
//    fun clearOkHttpClient() {
//        okHttpClient = null
//    }
//
//    internal fun makeDefaultOkHttpClient(): OkHttpClient {
//        val builder = OkHttpClient.Builder()
//        if (globalConfig.httpLog) builder.addInterceptor(NkHttpLog(globalConfig.saveToFile, globalConfig.logPath))
//        val client = builder.build();
//        okHttpClient = client
//        return client
//    }
//
//    internal fun okHttpClient(): OkHttpClient = okHttpClient ?: makeDefaultOkHttpClient()
}
//
//fun Any.post(path: () -> String) = NetKit.post(path()).also { it.tag = tagFilter(this) }
//fun Any.post(path: String?) = NetKit.post(path).also { it.tag = tagFilter(this) }
//fun Any.post(host: String?, path: String?) = NetKit.post(host, path).also { it.tag = tagFilter(this) }
//
//fun Any.get(path: () -> String) = NetKit.get(path()).also { it.tag = tagFilter(this) }
//fun Any.get(path: String?) = NetKit.get(path).also { it.tag = tagFilter(this) }
//fun Any.get(host: String?, path: String?) = NetKit.get(host, path).also { it.tag = tagFilter(this) }
//
//internal fun tagFilter(tag: Any?): Any? =
//        if (tag is Activity || tag is Fragment || tag is android.app.Fragment) tag
//        else null
