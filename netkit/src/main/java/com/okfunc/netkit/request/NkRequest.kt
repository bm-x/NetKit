package com.okfunc.netkit.request

import com.okfunc.netkit.*
import com.okfunc.netkit.convert.NkConvert
import okhttp3.Request
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KFunction

/**
 *
 * Created by buck on 2017/10/28.
 */


open class NkRequest<T>(val convert: NkConvert<T>) {

    var host: String? = null
    var path: String? = null

    val header = NkHeaders()
    val params = HashMap<String, Any>()

    val callbacks = ArrayList<NkCallback<Any>>(2)

    val cKeys = ArrayList<String>(4)
    val cValues = ArrayList<Any>(4)

    var feedBackProgress = false

    var requestAssemble: NkRequestAssemble<T>? = null

    init {
        NetKit.globalConfig.copyTo(this)
    }

    fun onSuccess(success: NK_SUCCESS<T>) = also { addfunc(K_SUCCESS, success) }

    fun onStart(start: NK_START<T>) = also { addfunc(K_START, start) }

    fun onFinish(finish: NK_FINISH<T>) = also { addfunc(K_FINISH, finish) }

    fun onError(error: NK_ERROR<T>) = also { addfunc(K_ERROR, error) }

    fun successOn(func: KFunction<Any>) = also { addfunc(K_SUCCESS, func) }

    fun startOn(func: KFunction<Any>) = also { addfunc(K_START, func) }

    fun finishOn(func: KFunction<Any>) = also { addfunc(K_FINISH, func) }

    fun errorOn(func: KFunction<Any>) = also { addfunc(K_ERROR, func) }

    fun callback(callback: NkCallback<T>) = also { callbacks.add(callback as NkCallback<Any>) }

    fun feedBackProgress() = also { feedBackProgress = true }

    fun host(host: String?) = also { this.host = host }

    fun path(path: String?) = also { this.path = path }

    fun url(host: String?, path: String?) = host(host).path(path)

    fun header(header: NkHeaders) = also { header.copyTo(this.header) }

    fun header(key: String, value: String) = also { header.set(key, value) }

    fun addHeader(key: String, value: String) = also { header.add(key, value) }

    fun addOnHeader(key: String, value: String) = also { header.addOn(key, value) }

    fun params(key: String, value: Any) = also { params[key] = value }

    fun get() = also { requestAssemble = NkGetAssemble<T>() }

    fun post() = also { requestAssemble = NkPostAssemble<T>() }

    fun end() {
        NkCall(this as NkRequest<Any>).start()
    }

    fun buildOkRequest(): Request {
        val builder = Request.Builder()
        header.copyTo(builder)
        builder.url(buildUrl())
        requestAssemble?.assemble(builder, this) ?: NkGetAssemble<T>().assemble(builder, this)
        return builder.build()
    }

    protected fun buildUrl(): String {
        val host = host ?: NetKit.globalConfig.host
        return if (host == null) path!! else "$host${path}"
    }

    fun clone(): NkRequest<T> {
        val n = NkRequest(convert)
        n.host = host
        n.path = path
        n.params.putAll(params)
        n.requestAssemble = requestAssemble
        header.clear()
        header.copyTo(n.header)
        return n
    }

    protected fun addfunc(key: String, func: Any) {
        cKeys.add(key)
        cValues.add(func)
    }

    inline internal fun eachFunc(key: String, block: (value: Any) -> Unit) {
        cKeys.indices.forEach { if (cKeys[it] == key) block(cValues[it]) }
    }

    companion object {
        internal const val K_SUCCESS = "1success"
        internal const val K_START = "2start"
        internal const val K_FINISH = "3finish"
        internal const val K_ERROR = "4error"
        internal const val K_DOWNLOAD_PROGRESS = "5download_progress"
        internal const val K_UPLOAD_PROGRESS = "6upload_progress"
    }
}
