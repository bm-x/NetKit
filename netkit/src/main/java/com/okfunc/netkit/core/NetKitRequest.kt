package com.okfunc.netkit.core

import java.util.concurrent.Executor

class NetKitRequest {

    private var _header: NetKitHeader? = null
    private fun _getHeader() = _header ?: NetKitHeader().apply { _header = this }

    private var _functions = NetKitFunctions()

    var autoStart = true
    var asynchronized = true

    var url: String? = null
    var protocol: String? = null
    var hostname: String? = null
    var port: Int? = null
    var path: String? = null
    val query = mutableMapOf<Any, Any?>()

    var method: String = "GET"

    var start: Function<*> by _functions
    var after_convert: Function<*> by _functions
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

    fun key(key: CharSequence): CharSequence = key

    operator fun Executor.plus(target: Function<*>) = if (target is WrapFunction) {
        target.threadMode(ThreadMode(ThreadMode.Executor, this))
    } else {

        WrapFunction(target, ThreadMode(ThreadMode.Executor, this))
    }

    operator fun CharSequence.plus(target: Function<*>) = if (target is WrapFunction) {
        target.name(this.toString())
    } else {
        WrapFunction(target, name = this.toString())
    }

    operator fun CharSequence.plus(target: ThreadMode) = WrapFunction(null, target, this.toString())
}