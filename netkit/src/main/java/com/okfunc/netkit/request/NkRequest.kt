package com.okfunc.netkit.request

import com.okfunc.netkit.NkHeaders
import com.okfunc.netkit.convert.NetKitConvert

/**
 *
 * Created by buck on 2017/10/28.
 */

open class NkRequest<T>(val convert: NetKitConvert<T>) : Request<NkRequest<T>> {

    protected var host: String? = null
    protected var path: String? = null

    protected val header = NkHeaders()
    protected val params = HashMap<String, Any>()

    override fun host(host: String?) = also { this.host = host }

    override fun path(path: String?) = also { this.path = path }

    override fun url(host: String?, path: String?) = host(host).path(path)

    override fun header(key: String, value: String) = also { header.set(key, value) }

    override fun addHeader(key: String, value: String) = also { header.add(key, value) }

    override fun addOnHeader(key: String, value: String) = also { header.addOn(key, value) }

    override fun params(key: String, value: Any) = also { params[key] = value }


}
