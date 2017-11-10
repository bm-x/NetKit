package com.okfunc.netkit.request

import com.okfunc.netkit.NetKit
import com.okfunc.netkit.NkHeaders
import com.okfunc.netkit.convert.NetKitConvert
import okhttp3.Request

/**
 *
 * Created by buck on 2017/10/28.
 */

open class NkRequest<T>(protected val convert: NetKitConvert<T>) : IRequest<NkRequest<T>> {

    var host: String? = null
    var path: String? = null

    val header = NkHeaders()
    val params = HashMap<String, Any>()

    var feedBackProgress = false

    protected var type: IRequest<NkRequest<T>>? = null

    init {
        NetKit.globalConfig.copyTo(this)
    }

    override fun feedBackProgress() = also { feedBackProgress = true }

    override fun host(host: String?) = also { this.host = host }

    override fun path(path: String?) = also { this.path = path }

    override fun url(host: String?, path: String?) = host(host).path(path)

    override fun header(header: NkHeaders) = also { header.copyTo(this.header) }

    override fun header(key: String, value: String) = also { header.set(key, value) }

    override fun addHeader(key: String, value: String) = also { header.add(key, value) }

    override fun addOnHeader(key: String, value: String) = also { header.addOn(key, value) }

    override fun params(key: String, value: Any) = also { params[key] = value }

    override fun get() = also { type = NkGetRequest(this) }

    override fun post() = also { type = NkPostRequest(this) }

    override fun buildOkRequest() = Request.Builder().also { builder ->

        header.copyTo(builder)
    }.build()

    override fun clone(base: IRequest<NkRequest<T>>) = clone()

    override fun clone(): IRequest<NkRequest<T>> {
        val n = NkRequest(convert)
        n.host = host
        n.path = path
        n.params.putAll(params)
        n.type = type?.clone(n)
        header.clear()
        header.copyTo(n.header)
        return n
    }
}
