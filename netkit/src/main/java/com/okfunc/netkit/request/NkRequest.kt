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
import kotlin.properties.Delegates
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
    var multipart: NkHeaders? = null

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

    fun runIf(run: Boolean, block: NkRequest<T>.() -> Unit) = also { if (run) this.block() }

    fun cachePolicy(policy: ICachePolicy) = also { cachePolicy = policy }

    fun cachePolicy(policy: CachePolicy) = also { cachePolicy = policy.policy() }

    fun tag(tag: Any?) = also { this.tag = tag }

    fun beforeSuccess(success: NK_BEFORE_SUCCESS<T>) = also { addfunc(K_BEFORE_SUCCESS, success) }

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

    fun url() = try {
        fullPath
    } catch (e: Throwable) {
        "$host$path"
    }

    fun header(header: NkHeaders) = also { header.copyTo(this.header) }

    fun header(key: String, value: String) = also { header.set(key, value) }

    fun addHeader(key: String, value: String) = also { header.add(key, value) }

    fun addOnHeader(key: String, value: String) = also { header.addOn(key, value) }

    fun params(key: String, value: Any) = also {
        mediaType = MEDIA_TYPE_FORM_URLENCODED
        params[key] = value
    }

    fun multipart(key: String, value: Any) = also {
        if (multipart == null) {
            multipart = NkHeaders()
        }
        multipart?.add(key, value.toString())
    }

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
        val assemble: NkRequestAssemble<T> = requestAssemble ?: NkGetAssemble<T>()
        fullPath = assemble.buildUrl(builder, this)
        encodeFullPath = URLEncoder.encode(fullPath)
        builder.url(fullPath)
        assemble.assemble(builder, this)
        return builder.build()
    }

    internal var fullPath: String by Delegates.notNull()
    internal var encodeFullPath: String by Delegates.notNull()

    protected fun buildUrl(): String {
        val host = host ?: NetKit.globalConfig.host
        val path = if (host == null) path!! else "$host${path}"
        val sb = StringBuilder(path)
        if (NetKit.globalConfig.params.isNotEmpty()) params.putAll(NetKit.globalConfig.params)
        if (params.isNotEmpty()) {
            if (!sb.contains('?')) {
                sb.append('?')
            }
            if (!sb.endsWith('&') && !sb.endsWith('?')) {
                sb.append('&')
            }
            for ((key, value) in params) {
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
        internal const val K_SUCCESS = "1_success"
        internal const val K_START = "2_start"
        internal const val K_FINISH = "3_finish"
        internal const val K_ERROR = "4_error"
        internal const val K_DOWNLOAD_PROGRESS = "5_download_progress"
        internal const val K_UPLOAD_PROGRESS = "6_upload_progress"
        internal const val K_BEFORE_SUCCESS = "7_before_success"
    }
}
