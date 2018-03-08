package com.okfunc.netkit.request

import com.okfunc.netkit.NetKit
import okhttp3.Request

/**
 * Created by bm on 2017/11/15.
 */
class NkGetAssemble<T> : NkRequestAssemble<T> {
    override fun buildUrl(builder: Request.Builder, req: NkRequest<T>): String {
        val host = req.host ?: NetKit.globalConfig.host
        val path = if (host == null) req.path!! else "$host${req.path}"
        val sb = StringBuilder(path)
        if (NetKit.globalConfig.params.isNotEmpty()) req.params.putAll(NetKit.globalConfig.params)
        if (req.params.isNotEmpty()) {
            if (!sb.contains('?')) {
                sb.append('?')
            }
            if (sb.contains('&') && !sb.endsWith('&')) {
                sb.append('&')
            }
            for ((key, value) in req.params) {
                sb.append(key)
                sb.append('=')
                sb.append(value)
                sb.append('&')
            }
            sb.deleteCharAt(sb.length - 1)
        }

        return sb.toString()
    }

    override fun assemble(builder: Request.Builder, req: NkRequest<T>) {
        builder.get()
    }
}