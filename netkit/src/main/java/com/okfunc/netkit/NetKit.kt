package com.okfunc.netkit

import okhttp3.OkHttpClient


/**
 * Created by buck on 2017/10/28.
 */
object NetKit {

    var globalConfig: NkConfig = NkConfig()
    var okHttpClient: OkHttpClient? = null

    fun request(path: String?) = NetKitUrl(null, path)

    fun request(host: String?, path: String?) = NetKitUrl(null, null)

    fun get(path: String?) = NetKitGetUrl(path)

    fun get(host: String?, path: String?) = NetKitGetUrl(host, path)

    fun post(path: String?) = NetKitPostUrl(path)

    fun post(host: String?, path: String?) = NetKitPostUrl(host, path)

    internal fun makeDefaultOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val client = builder.build();
        okHttpClient = client
        return client
    }

    internal fun okHttpClient(): OkHttpClient = okHttpClient ?: makeDefaultOkHttpClient()


}