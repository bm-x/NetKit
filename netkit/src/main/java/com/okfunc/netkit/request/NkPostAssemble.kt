package com.okfunc.netkit.request

import com.okfunc.netkit.NetKit
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody

/**
 * Created by bm on 2017/11/15.
 */
class NkPostAssemble<T> : NkRequestAssemble<T> {
    override fun buildUrl(builder: Request.Builder, req: NkRequest<T>): String {
        val host = req.host ?: NetKit.globalConfig.host
        val path = if (host == null) req.path!! else "$host${req.path}"
        val sb = StringBuilder(path)
        if (NetKit.globalConfig.params.isNotEmpty()) {
            if (!sb.contains('?')) {
                sb.append('?')
            }
            if (!sb.endsWith('&') && !sb.endsWith('?')) {
                sb.append('&')
            }
            NetKit.globalConfig.params.forEach {
                sb.append(it.key)
                sb.append('=')
                sb.append(it.value)
                sb.append('&')
            }
            if (sb.endsWith('&')) {
                sb.deleteCharAt(sb.length - 1)
            }
        }
        return sb.toString()
    }

    override fun assemble(builder: Request.Builder, req: NkRequest<T>) {
        if (req.mediaType != null && (req.params.isNotEmpty() || req.postContent != null)) {
            if (req.params.isNotEmpty()) {
                builder.post(RequestBody.create(req.mediaType, buildParamsContent(req.params)))
            } else if (req.postContent != null) {
                builder.post(RequestBody.create(req.mediaType, req.postContent))
            }
        } else if (req.multipart != null) {
            builder.post(MultipartBody.Builder().setType(MultipartBody.FORM)
                    .also {
                        req.multipart?.forEach { key, value ->
                            it.addFormDataPart(key, value)
                        }
                    }.build())
        }
        if (req.mediaType != null) {
            builder.addHeader("Content-Type", req.mediaType?.toString())
        }
    }

    fun buildParamsContent(map: HashMap<String, Any>): String {
        val sb = StringBuilder()
        map.forEach {
            sb.append(it.key)
            sb.append('=')
            sb.append(it.value)
            sb.append('&')
        }
        if (sb.endsWith('&')) {
            sb.deleteCharAt(sb.length - 1)
        }
        return sb.toString()
    }
}