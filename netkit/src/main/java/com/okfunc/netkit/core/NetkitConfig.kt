package com.okfunc.netkit.core

import com.okfunc.netkit.convert.NetKitConvert
import com.okfunc.netkit.core.utils.VirtualPairMap


class NetkitConfig : (NetkitConfig) -> Unit {

    var autoStart = true
    var asynchronized = true

    var url: String? = null
    var protocol: String? = null
    var host: String? = null
    var port: Int? = null
    var path: String? = null

    var method: String = "GET"

    val query = VirtualPairMap<Any, Any?>()

    var convert: NetKitConvert? = null

    override fun invoke(p1: NetkitConfig) {

    }
}
