package com.okfunc.netkit.convert

import com.okfunc.netkit.core.NetKitRequest
import com.okfunc.netkit.core.NetKitRespone
import okhttp3.Response

interface NetKitConvert : Function<Any> {
    var target: Function<*>?
    fun onConvert(okResponse: Response, nkRequest: NetKitRequest, nkResponse: NetKitRespone<*>)
}