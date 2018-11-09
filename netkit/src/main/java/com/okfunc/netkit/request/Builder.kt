package com.okfunc.netkit.request

import com.okfunc.netkit.get
import java.lang.StringBuilder
import kotlin.reflect.KProperty

interface IVirtualMap<K, V> {
    operator fun get(key: K): V
    operator fun set(key: K, value: V)
}

open class VirtualMap : IVirtualMap<Any, Any?> {

    val entrys = mutableListOf<Pair<String, String?>>()

    override fun set(key: Any, value: Any?) {
        entrys += key.toString() to value?.toString()
    }

    override fun get(key: Any): Any? {
        val _key = key.toString()
        val list = entrys.filter { it.first == _key }
        if (list.isEmpty()) return null
        else if (list.size == 1) return list[0].second
        else {
            val sb = StringBuilder()
            list.forEach {
                sb.append(it.second)
                sb.append('；')
            }
            sb.deleteCharAt(sb.length - 1)
            return sb.toString()
        }
    }

    private fun KProperty<*>.httpHeaderName() = name.replace('_', '-')

    operator fun getValue(netKitHeader: NetKitHeader, property: KProperty<*>) =
            get(property.httpHeaderName()) as? String

    operator fun setValue(netKitHeader: NetKitHeader, property: KProperty<*>, s: Any?) {
        set(property.httpHeaderName(), s)
    }
}

class NetKitHeader : VirtualMap() {
    var Accept by this
    var Accept_Charset by this
    var Accept_Encoding by this
    var Accept_Language by this
    var Authorization by this
    var Connection by this
    var Content_Length by this
    var Cookie by this
    var From by this
    var Host by this
    var If_Modified_Since by this
    var Pragma by this
    var Referer by this
    var User_Agent by this
}

class NetKitBuilder {

    private var _header: NetKitHeader? = null
    private fun _getHeader() = _header ?: NetKitHeader().apply { _header = this }

    var url: String? = null
    var protocol: String? = null
    var hostname: String? = null
    var port: Int? = null
    var path: String? = null
    val query = mutableMapOf<Any, Any?>()

    var method: String = "GET"

    var asynchronized = true

    var success: Any = ""

    var header: (NetKitHeader.() -> Unit) = {}
        set(value) = _getHeader().value()

    operator fun (NetKitHeader.() -> Unit).get(key: Any) = _getHeader()[key]
    operator fun (NetKitHeader.() -> Unit).set(key: Any, value: Any?) {
        _getHeader()[key] = value
    }

}

fun Any.request(block: NetKitBuilder.() -> Unit) {
    NetKitBuilder().block()
}


fun request(method: String? = null,
            url: String? = null,
            block: NetKitBuilder.() -> Unit) = NetKitBuilder().block()

fun main(args: Array<String>) {
//    request {
//        url = "http://www.baidu.com"
//        query["name"] = "123"
//        query["age"] = 18
//        method = "GET"
//
//        header = {
//            Accept = "xml"
//            Accept = "html"
//        }
//
//        success = {
//
//        }
//
//        println(header["Accept"])
//    }
}