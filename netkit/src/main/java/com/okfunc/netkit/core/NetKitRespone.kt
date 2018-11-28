package com.okfunc.netkit.core

import kotlin.properties.Delegates

class NetKitRespone<T>(
        val request: NetKitRequest
) {
    var code: Int = -1

    val target: T? = null
}