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
        if (NetKit.globalConfig.querys.isNotEmpty()) req.querys.putAll(NetKit.globalConfig.querys)
        if (req.querys.isNotEmpty()) {
            if (!sb.contains('?')) {
                sb.append('?')
            }
            if (sb.contains('&') && !sb.endsWith('&')) {
                sb.append('&')
            }
            sb.append(buildParamsString(req.querys))
        }
        return sb.toString()
    }

    override fun assemble(builder: Request.Builder, req: NkRequest<T>) {
        builder.get()
    }
}