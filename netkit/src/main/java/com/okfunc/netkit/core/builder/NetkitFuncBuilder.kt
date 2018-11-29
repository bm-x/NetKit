package com.okfunc.netkit.core.builder

import com.okfunc.netkit.convert.NetKitConvert
import com.okfunc.netkit.core.NetKitFunctions
import com.okfunc.netkit.core.NetKitHeader
import com.okfunc.netkit.core.ThreadMode
import com.okfunc.netkit.core.WrapFunction
import com.okfunc.netkit.core.utils.VirtualPairMap
import okhttp3.MediaType
import java.util.concurrent.Executor

class NetkitFuncBuilder : NetkitBuilder() {

    private var _header: NetKitHeader? = null
    private fun _getHeader() = _header ?: NetKitHeader().apply { _header = this }

    private var _functions = NetKitFunctions()

    var convert: NetKitConvert? = null

    var autoStart = true
    var asynchronized = true

    override var url: String? = null
    override var protocol: String? = null
    override var host: String? = null
    override var port: Int? = null
    override var path: String? = null

    override val query = VirtualPairMap<Any, Any?>()

    override var method: String = "GET"

    override var contentType: MediaType? = null
    override var content: Any? = null

    val params = VirtualPairMap<String, Any?>(0)

    var json: String?
        get() = content as? String
        set(value) {
            content = value
            contentType = MEDIA_TYPE_JSON
        }

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

    override fun finish() {
        if (content != null && contentType != null && !params.isEmpty()) {
            content = params.buildParams()
            contentType = MEDIA_TYPE_FORM_URLENCODED
        }
    }

    override fun functions() = _functions

    override fun header() = _header
}