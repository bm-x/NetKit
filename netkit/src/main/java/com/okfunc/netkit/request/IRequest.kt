package com.okfunc.netkit.request

import com.okfunc.netkit.NkHeaders
import okhttp3.Request

/**
 * Created by buck on 2017/11/3.
 */

interface IRequest<T> {
    fun host(host: String?): T

    fun path(path: String?): T

    fun url(host: String?, path: String?): T

    fun header(header: NkHeaders): T

    fun header(key: String, value: String): T

    fun addHeader(key: String, value: String): T

    fun addOnHeader(key: String, value: String): T

    fun params(key: String, value: Any): T

    fun feedBackProgress(): T

    fun buildOkRequest(): Request

    fun post(): T

    fun get(): T

    fun clone(): IRequest<T>

    fun clone(base: IRequest<T>): IRequest<T>

}