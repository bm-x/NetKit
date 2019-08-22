package com.okfunc.netkit.request

import com.okfunc.netkit.MEDIA_TYPE_PLAIN
import com.okfunc.netkit.MEDIA_TYPE_STREAM
import com.okfunc.netkit.NetKit
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File

/**
 * Created by bm on 2017/11/15.
 */
class NkPostAssemble<T> : NkRequestAssemble<T> {
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
            if (!sb.endsWith('&') && !sb.endsWith('?')) {
                sb.append('&')
            }
            sb.append(buildParamsString(req.querys))
        }
        return sb.toString()
    }

    override fun assemble(builder: Request.Builder, req: NkRequest<T>) {
        if (req.mediaType != null && (req.params.isNotEmpty() || req.postContent != null)) {
            if (req.params.isNotEmpty()) {
                builder.post(RequestBody.create(req.mediaType, buildParamsString(req.params)))
            } else if (req.postContent != null) {
                builder.post(RequestBody.create(req.mediaType, req.postContent))
            }
        } else if (req.multipart != null) {
            builder.post(MultipartBody.Builder().setType(MultipartBody.FORM)
                    .also { b ->
                        req.multipart?.forEach {
                            val second = it.second
                            if (second is File) {
                                b.addFormDataPart(it.first, second.name, RequestBody.create(MEDIA_TYPE_STREAM, second))
                            } else {
                                b.addFormDataPart(it.first, second.toString())
                            }
                        }
                    }.build())
        } else {
            builder.post(RequestBody.create(MEDIA_TYPE_PLAIN, ""))
        }
        if (req.mediaType != null) {
            builder.addHeader("Content-Type", req.mediaType?.toString())
        }
    }
}