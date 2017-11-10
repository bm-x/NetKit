package com.okfunc.netkit

import com.okfunc.netkit.request.NkRequest

/**
 * Created by buck on 2017/11/8.
 */

class NkConfig() {

    constructor(initBlock: NkConfig.() -> Unit) : this() {
        initBlock()
    }

    protected val header = NkHeaders()
    protected val params = HashMap<String, Any>()
    protected var cacheMode = CacheMode.NONE

    fun header(key: String, value: String) = also { header.set(key, value) }

    fun addHeader(key: String, value: String) = also { header.add(key, value) }

    fun addOnHeader(key: String, value: String) = also { header.addOn(key, value) }

    fun cacheMode() {

    }

    fun copyTo(request: NkRequest<*>) {
        request.header(header)
    }

    enum class CacheMode {
        NONE,
        DIFF_STREAM_BYTE
    }
}