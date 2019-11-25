package com.okfunc.netkit

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import okhttp3.OkHttpClient

val DEFAULT_CONFIG = NkConfig {
    default = true
}

/**
 *
 * Created by buck on 2017/10/28.
 */
@SuppressLint("StaticFieldLeak")
object NetKit {

    var globalConfig: NkConfig = DEFAULT_CONFIG
        set(value) {
            if (value.application == null) value.application = field.application
            field = value
            clearOkHttpClient()
        }

    var okHttpClient: OkHttpClient? = null

    fun cancel(tag: Any) = NkController.cancel(tag)

    fun cancelAll() = NkController.cancelAll()

    fun request(path: String?) = NetKitUrl(null, path)

    fun request(host: String?, path: String?) = NetKitUrl(null, null)

    fun get(path: String?) = NetKitGetUrl(path)

    fun get(host: String?, path: String?) = NetKitGetUrl(host, path)

    fun post(path: String?) = NetKitPostUrl(path)

    fun post(host: String?, path: String?) = NetKitPostUrl(host, path)

    fun application(app: Application) {
        globalConfig.application = app
    }

    fun clearOkHttpClient() {
        okHttpClient = null
    }

    internal fun makeDefaultOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (globalConfig.httpLog) builder.addInterceptor(NkHttpLog(globalConfig.saveToFile, globalConfig.logPath))
        globalConfig.onBuildOkHttpClient(builder)
        val client = builder.build();
        okHttpClient = client
        return client
    }

    fun okHttpClient(): OkHttpClient = okHttpClient ?: makeDefaultOkHttpClient()
}

fun Any.post(path: () -> String) = NetKit.post(path())
fun Any.post(path: String?) = NetKit.post(path)
fun Any.post(host: String?, path: String?) = NetKit.post(host, path)

fun Any.get(path: () -> String) = NetKit.get(path())
fun Any.get(path: String?) = NetKit.get(path)
fun Any.get(host: String?, path: String?) = NetKit.get(host, path)

internal fun tagFilter(tag: Any?): Any? =
        if (tag is Activity || tag is Fragment || tag is android.app.Fragment) tag
        else null
