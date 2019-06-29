package com.okfunc.netkit.request

import okhttp3.Request

/**
 * Created by bm on 2017/11/15.
 */
interface NkRequestAssemble<T> {
    fun assemble(builder: Request.Builder, req: NkRequest<T>)

    fun buildUrl(builder: Request.Builder, req: NkRequest<T>): String

    fun buildParamsString(map: Map<String, Any?>): String {
        val sb = StringBuilder()
        for ((key, value) in map) {
            sb.append(key)
            sb.append('=')
            sb.append(value)
            sb.append('&')
        }
        if (sb.isNotEmpty()) sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }
}