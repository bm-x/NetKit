package com.okfunc.netkit.request

import com.okfunc.netkit.NetKit
import okhttp3.Request

/**
 * Created by bm on 2017/11/15.
 */
class NkGetAssemble<T> : NkRequestAssemble<T> {
    override fun buildUrl(builder: Request.Builder, req: NkRequest<T>): String {
        val sb = StringBuilder()
        val host = req.host ?: NetKit.globalConfig.host
        val path = req.path ?: ""
        if (host == null || host.length == 0) {
            sb.append(req.path)
        } else {
            sb.append(host)
            if (sb.endsWith('/') && path.startsWith('/')) sb.deleteCharAt(sb.lastIndex)
            else if (!sb.endsWith('/') && !path.startsWith('/')) sb.append('/')
            sb.append(path)
        }
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