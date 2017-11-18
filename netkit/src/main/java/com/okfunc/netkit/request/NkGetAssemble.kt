package com.okfunc.netkit.request

import okhttp3.Request

/**
 * Created by bm on 2017/11/15.
 */
class NkGetAssemble<T> : NkRequestAssemble<T> {
    override fun assemble(builder: Request.Builder, req: NkRequest<T>) {
        builder.get()
    }
}