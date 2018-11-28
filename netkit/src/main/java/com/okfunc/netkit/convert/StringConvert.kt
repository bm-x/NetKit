package com.okfunc.netkit.convert

import com.okfunc.netkit.core.NetKitRequest
import com.okfunc.netkit.core.NetKitRespone
import okhttp3.Response

class StringConvert(
        override var target: Function<*>?
) : NetKitConvert, Function<Any> {

    override fun onConvert(okResponse: Response, nkRequest: NetKitRequest, nkResponse: NetKitRespone<*>) {

    }
}

fun NetKitRequest.stringConvert(func: Function<*>) = StringConvert(func)
fun NetKitRequest.stringConvert(func: (String) -> Unit) = StringConvert(func)
