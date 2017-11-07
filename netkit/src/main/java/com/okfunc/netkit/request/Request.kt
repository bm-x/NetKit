package com.okfunc.netkit.request

/**
 * Created by buck on 2017/11/3.
 */

interface Request<T> {
    fun host(host: String?): T

    fun path(path: String?): T

    fun url(host: String?, path: String?): T

    fun header(key: String, value: String): T

    fun addHeader(key: String, value: String): T

    fun addOnHeader(key: String, value: String): T

    fun params(key: String, value: Any): T

}