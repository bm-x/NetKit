package com.okfunc.netkit.request

import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody

/**
 * Created by bm on 2017/11/15.
 */
class NkPostAssemble<T> : NkRequestAssemble<T> {
    override fun assemble(builder: Request.Builder, req: NkRequest<T>) {
        if (req.postContent != null && req.mediaType != null) {
            builder.post(RequestBody.create(req.mediaType, req.postContent))
        } else if (req.multipart != null) {
            builder.post(MultipartBody.Builder().setType(MultipartBody.FORM)
                    .also {
                        req.multipart?.forEach { key, value ->
                            it.addFormDataPart(key, value)
                        }
                    }.build())
        }
    }
}