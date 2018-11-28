package com.okfunc.netkit.request

import com.okfunc.netkit.NkIgnore
import com.okfunc.netkit.error.NetkitForbidException
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.collections.set
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

interface IVirtualMap<K, V> {
    operator fun get(key: K): V
    operator fun set(key: K, value: V)
}

class NetKitFunctions {
    val functions = mutableListOf<Pair<String, Function<*>>>()

    operator fun getValue(netKitBuilder: NetKitRequest, property: KProperty<*>): Function<*> {
        throw NetkitForbidException("The get() method is not allowed to be invoked on ${property.name} feild")
    }

    operator fun setValue(netKitBuilder: NetKitRequest, property: KProperty<*>, function: Function<*>) {
        functions += property.name to (function as? WrapFunction ?: when (property.name) {
            "before_success" -> WrapFunction(function, ThreadMode(ThreadMode.Default))
            else -> WrapFunction(function, ThreadMode(ThreadMode.UI))
        })
    }
}

class NetKitHeader : IVirtualMap<Any, Any?> {
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
                sb.append(';')
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

class NetKitRequest {
    private var _header: NetKitHeader? = null
    private fun _getHeader() = _header ?: NetKitHeader().apply { _header = this }

    private var _functions = NetKitFunctions()

    var url: String? = null
    var protocol: String? = null
    var hostname: String? = null
    var port: Int? = null
    var path: String? = null
    val query = mutableMapOf<Any, Any?>()

    var method: String = "GET"

    var asynchronized = true

    var start: Function<*> by _functions
    var before_success: Function<*> by _functions
    var success: Function<*> by _functions
    var error: Function<*> by _functions
    var finish: Function<*> by _functions

    var header: (NetKitHeader.() -> Unit) = {}
        set(value) = _getHeader().value()

    operator fun (NetKitHeader.() -> Unit).get(key: Any) = _getHeader()[key]
    operator fun (NetKitHeader.() -> Unit).set(key: Any, value: Any?) {
        _getHeader()[key] = value
    }

    val UI: ThreadMode get() = ThreadMode(ThreadMode.UI)
    val Default: ThreadMode get() = ThreadMode(ThreadMode.Default)
    val Thread: ThreadMode get() = ThreadMode(ThreadMode.Thread)

    operator fun Executor.plus(target: Function<*>): Function<*> {
        if (target is WrapFunction) {
            target.threadMode = ThreadMode(ThreadMode.Executor, this)
            return target
        } else {
            return WrapFunction(target, ThreadMode(ThreadMode.Executor, this))
        }
    }
}

class NetKitRespone<T : Any>(val block: NetKitRespone<T>.() -> Unit) : Function<T> {
    val target: T by Delegates.notNull()
}

fun <T : Any> objectDecoration(block: NetKitRespone<T>.() -> Unit): Function<T> {
    return NetKitRespone(block)
}

fun Any.request(block: NetKitRequest.() -> Unit) {
    NetKitRequest().block()
}

fun request(method: String? = null,
            url: String? = null,
            block: NetKitRequest.() -> Unit) = NetKitRequest().block()

val executors = Executors.newFixedThreadPool(3)

fun main(args: Array<String>) {

    request {
        url = "http://www.baidu.com"
        query["name"] = "123"
        query["age"] = 18
        method = "GET"

        asynchronized = false

        header = {
            Accept = "xml"
            Accept = "html"
        }

        finish = executors + {

        }

        before_success = Default + {

        }

        success = UI + objectDecoration<NkIgnore> {

        }

        error = Thread + {

        }
    }
}
