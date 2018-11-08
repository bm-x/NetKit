package com.okfunc.netkit.request

open class VirtualMap {
    operator fun set(key: Any, value: Any?) {

    }
}

class NetKitHeader : VirtualMap() {
    var Accept: String? = null
    var Accept_Charset: String? = null
}

class NetKitBuilder {
    var url: String? = null
    var protocol: String? = null
    var hostname: String? = null
    var port: Int? = null
    var path: String? = null
    val query = mutableMapOf<Any, Any?>()

    fun header(init: NetKitHeader.() -> Unit) {
        val h = _header ?: NetKitHeader()

    }

    private var _header: NetKitHeader? = null
}

fun Any.get(block: NetKitBuilder.() -> Unit) {
    NetKitBuilder().block()
}

fun get(block: NetKitBuilder.() -> Unit) = NetKitBuilder().block()

fun a() {

    get {
        url = "http://www.baidu.com"
        query["name"] = "123"
        query["age"] = 18

        header {
            Accept = ""
            Accept_Charset = "utf8"
        }
    }
}