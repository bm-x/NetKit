package com.okfunc.netkit.core.builder

import android.media.MediaDrm
import com.okfunc.netkit.*
import com.okfunc.netkit.convert.NetKitConvert
import com.okfunc.netkit.core.*
import com.okfunc.netkit.core.utils.IVirtualMap
import com.okfunc.netkit.core.utils.VirtualPairMap
import okhttp3.MediaType
import java.util.concurrent.Executor

class NetkitFuncBuilder : NetkitBuilder() {

    private var _header = NetKitHeader()

    private var _config: NetkitConfig? = null

    private var _functions = NetKitFunctions()

    var header: (NetKitHeader.() -> Unit) = {}
        set(value) = _header.value()

    var (NetKitHeader.() -> Unit).Accept by _header
    operator fun (NetKitHeader.() -> Unit).get(key: Any) = _header[key]
    operator fun (NetKitHeader.() -> Unit).set(key: Any, value: Any?) {
        _header[key] = value
    }

    var config: (NetkitConfig.() -> Unit)? = {}
        set(value) {
            field = value
            if (value == null) return
            if (value is NetkitConfig) {
                _config = value
            } else {
                if (_config == null) _config = NetkitConfig()
                _config?.value()
            }
        }

    var convert: NetKitConvert? = null

    override var autoStart: Boolean? = null
    override var asynchronized: Boolean? = null

    override var url: String? = null
    override var protocol: String? = null
    override var host: String? = null
    override var port: Int? = null
    override var path: String? = null

    override val query = VirtualPairMap<Any, Any?>()

    override var method: String? = null

    override var contentType: MediaType? = null
    override var content: Any? = null

    override val params = VirtualPairMap<Any, Any?>(0)

    val form = object : IVirtualMap<Any, Any?> {
        override fun get(key: Any) = params[key]
        override fun set(key: Any, value: Any?) {
            params[key] = value
            contentType = MEDIA_TYPE_FORM_URLENCODED
        }
    }

    val multipartForm = object : IVirtualMap<Any, Any?> {
        override fun get(key: Any) = params[key]
        override fun set(key: Any, value: Any?) {
            params[key] = value
            contentType = MEDIA_TYPE_FORM
        }
    }

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

    fun start(func: (req: MediaDrm.KeyRequest, res: NkResponse) -> Unit) = _functions.setValue(this, start::javaClass, func)

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

        if (_config == null) _config = NetKit.globalConfig

        val config = _config ?: return

        if (url == null) url = config.url
        if (protocol == null) protocol = config.protocol
        if (host == null) host = config.host
        if (port == null) port = config.port
        if (path == null) path = config.path
        if (method == null) method = config.method
        if (contentType == null) contentType = config.contentType

        _header.entrys.addAll(config.header.entrys)
        query.list.addAll(config.query.list)
        params.list.addAll(config.params.list)

        if (convert == null) convert = config.convert
        if (autoStart == null) autoStart = config.autoStart
        if (asynchronized == null) asynchronized = config.asynchronized
    }

    override fun functions() = _functions

    override fun header() = _header

    override fun config() = _config
}