package com.okfunc.netkit.request

import com.okfunc.netkit.*
import com.okfunc.netkit.cache.CachePolicy
import com.okfunc.netkit.cache.ICachePolicy
import com.okfunc.netkit.convert.NkConvert
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.Request
import java.net.URL
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KFunction

/**
 *
 * Created by buck on 2017/10/28.
 */


open class NkRequest<T>(val convert: NkConvert<T>) {

    var tag: Any? = null

    var host: String? = null
    var path: String? = null

    val header = NkHeaders()
    val params = HashMap<String, Any>()

    var postContent: String? = null
    var mediaType: MediaType? = null

    val callbacks = ArrayList<NkCallback<Any>>(2)

    val cKeys = ArrayList<String>(4)
    val cValues = ArrayList<Any>(4)

    var feedBackProgress = false

    var requestAssemble: NkRequestAssemble<T>? = null

    var cachePolicy: ICachePolicy? = null

    init {
        NetKit.globalConfig.copyTo(this)
    }

    fun cachePolicy(policy: ICachePolicy) = also { cachePolicy = policy }

    fun cachePolicy(policy: CachePolicy) = also { cachePolicy = policy.policy() }

    fun tag(tag: Any?) = also { this.tag = tag }

    fun onSuccess(success: NK_SUCCESS<T>) = also { addfunc(K_SUCCESS, success) }

    fun onStart(start: NK_START<T>) = also { addfunc(K_START, start) }

    fun onFinish(finish: NK_FINISH<T>) = also { addfunc(K_FINISH, finish) }

    fun onError(error: NK_ERROR<T>) = also { addfunc(K_ERROR, error) }

    fun success(func: KFunction<Any>) = also { addfunc(K_SUCCESS, func) }

    fun start(func: KFunction<Any>) = also { addfunc(K_START, func) }

    fun finish(func: KFunction<Any>) = also { addfunc(K_FINISH, func) }

    fun error(func: KFunction<Any>) = also { addfunc(K_ERROR, func) }

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

    fun json(json: String) = also {
        mediaType = MEDIA_TYPE_JSON
        postContent = json
    }

    fun end() {
        NkController.enqueue(this as NkRequest<Any>)
    }

    fun buildOkRequest(): Request {
        val builder = Request.Builder()
        header.copyTo(builder)
        builder.url(fullPath)
        requestAssemble?.assemble(builder, this) ?: NkGetAssemble<T>().assemble(builder, this)
        return builder.build()
    }

    internal val fullPath: String by lazy { buildUrl() }
    internal val encodeFullPath: String by lazy { URLEncoder.encode(fullPath, UTF8) }

    protected fun buildUrl(): String {
        val host = host ?: NetKit.globalConfig.host
        val path = if (host == null) path!! else "$host${path}"
        val sb = StringBuilder(path)
        if (NetKit.globalConfig.params.isNotEmpty()) {
            if (!sb.contains('?')) {
                sb.append('?')
            }
            if (sb.contains('&') && !sb.endsWith('&')) {
                sb.append('&')
            }
            for ((key, value) in NetKit.globalConfig.params) {
                sb.append(key)
                sb.append('=')
                sb.append(value)
                sb.append('&')
            }
            sb.deleteCharAt(sb.length - 1)
        }
        return sb.toString()
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
