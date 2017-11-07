package com.okfunc.netkit

import com.okfunc.netkit.convert.NetKitConvert
import com.okfunc.netkit.convert.StringConvert
import com.okfunc.netkit.request.NkRequest
import com.okfunc.netkit.NetKit.POST
import com.okfunc.netkit.request.NkPostRequest

/**
 *
 * Created by buck on 2017/11/2.
 */
class NetKitUrl(val host: String?, val path: String?) {

    private var method = "NONE"

    fun get() = also { method = NetKit.GET }
    fun post() = also { method = NetKit.POST }
    fun put() = also { method = NetKit.PUT }
    fun delete() = also { method = NetKit.DELETE }

    fun <T> convert(convert: NetKitConvert<T>): NkRequest<T> {
        val request = when (method) {
            POST -> NkPostRequest(convert)
            else -> NkRequest(convert)
        }
        request.url(host, path)
        return request
    }

    fun stringConvert() = convert(StringConvert())

}

