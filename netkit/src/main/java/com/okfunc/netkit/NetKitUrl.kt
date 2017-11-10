package com.okfunc.netkit

import com.okfunc.netkit.convert.NetKitConvert
import com.okfunc.netkit.convert.StringConvert
import com.okfunc.netkit.request.NkRequest

/**
 *
 * Created by buck on 2017/11/2.
 */

open class NetKitUrl(val host: String?, val path: String?) {
    constructor(path: String?) : this(null, path)
    constructor() : this(null, null)

    open fun <T> convert(convert: NetKitConvert<T>) = NkRequest(convert).url(host, path)

    fun stringConvert() = convert(StringConvert())
}

open class NetKitGetUrl(host: String?, path: String?) : NetKitUrl(host, path) {
    constructor(path: String?) : this(null, path)

    override fun <T> convert(convert: NetKitConvert<T>) = super.convert(convert).get()
}

open class NetKitPostUrl(host: String?, path: String?) : NetKitUrl(host, path) {
    constructor(path: String?) : this(null, path)

    override fun <T> convert(convert: NetKitConvert<T>) = super.convert(convert).post()
}

