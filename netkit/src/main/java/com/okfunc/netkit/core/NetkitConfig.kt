package com.okfunc.netkit.core

import com.okfunc.netkit.convert.NetKitConvert
import com.okfunc.netkit.core.utils.VirtualPairMap
import okhttp3.MediaType


class NetkitConfig(init: (NetkitConfig.() -> Unit)? = null) : (NetkitConfig) -> Unit {

    override fun invoke(p1: NetkitConfig) = Unit

    init {
        if (init != null) this.init()
    }

    var url: String? = null
    var protocol: String? = null
    var host: String? = null
    var port: Int? = null
    var path: String? = null
    var method: String = "GET"

    val header = NetKitHeader()

    val query = VirtualPairMap<Any, Any?>()
    val params = VirtualPairMap<String, Any?>(0)

    var contentType: MediaType? = null

    var convert: NetKitConvert? = null

    var autoStart = true
    var asynchronized = true

}