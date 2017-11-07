package com.okfunc.netkit


/**
 * Created by buck on 2017/10/28.
 */
object NetKit {

    fun request(path: String? = null) = NetKitUrl(null, path)

    fun request(host: String? = null, path: String? = null) = NetKitUrl(null, null)


    val GET = "get"
    val POST = "post"
    val DELETE = "delete"
    val PUT = "put"
}