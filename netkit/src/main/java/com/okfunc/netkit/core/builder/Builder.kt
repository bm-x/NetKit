package com.okfunc.netkit.core.builder

import com.okfunc.netkit.convert.stringConvert
import com.okfunc.netkit.core.*
import com.okfunc.netkit.core.utils.VirtualPairMap
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okio.ByteString
import java.io.File
import java.lang.StringBuilder
import java.net.URL
import java.util.concurrent.Executors

abstract class NetkitBuilder {
    abstract var url: String?
    abstract var protocol: String?
    abstract var host: String?
    abstract var port: Int?
    abstract var path: String?
    abstract var method: String

    abstract var content: Any?
    abstract var contentType: MediaType?

    abstract val query: VirtualPairMap<Any, Any?>

    internal abstract fun finish()
    internal abstract fun header(): NetKitHeader?
    internal abstract fun functions(): NetKitFunctions

    companion object {
        val MEDIA_TYPE_PLAIN get() = MediaType.parse("text/plain;charset=utf-8")
        val MEDIA_TYPE_JSON get() = MediaType.parse("application/json;charset=utf-8")
        val MEDIA_TYPE_STREAM get() = MediaType.parse("application/octet-stream")
        val MEDIA_TYPE_FORM_URLENCODED get() = MediaType.parse("application/x-www-form-urlencoded")
    }
}

fun VirtualPairMap<*, *>.buildParams(): String {
    val sb = StringBuilder()
    foreach {
        sb.append(it.first.toString())
        sb.append('=')
        sb.append(it.second?.toString())
        sb.append('&')
    }
    if (sb.isNotEmpty()) sb.deleteCharAt(sb.length - 1)
    return sb.toString()
}

internal fun buildNetkitRequest(nkBuilder: NetkitBuilder): NetKitRequest {

    nkBuilder.finish()

    val okBuilder = Request.Builder()

    val urlBuilder = try {
        HttpUrl.get(nkBuilder.url!!).newBuilder()
    } catch (e: Throwable) {
        val builder = HttpUrl.Builder()
        if (nkBuilder.protocol != null) builder.scheme(nkBuilder.protocol!!)
        if (nkBuilder.host != null) builder.host(nkBuilder.host!!)
        if (nkBuilder.port != null) builder.port(nkBuilder.port!!)
        if (nkBuilder.path != null) builder.addPathSegment(nkBuilder.path!!)
        builder
    }

    nkBuilder.query.foreach {
        urlBuilder.addQueryParameter(it.first.toString(), it.second?.toString())
    }

    okBuilder.url(urlBuilder.build())

    okBuilder.method(nkBuilder.method, when (nkBuilder.method) {
        "POST" -> when {
            nkBuilder.content != null && nkBuilder.contentType != null -> {
                val content = nkBuilder.content
                when (content) {
                    is CharSequence -> RequestBody.create(nkBuilder.contentType, content.toString())
                    is ByteString -> RequestBody.create(nkBuilder.contentType, content)
                    is ByteArray -> RequestBody.create(nkBuilder.contentType, content)
                    is File -> RequestBody.create(nkBuilder.contentType, content)
                    else -> RequestBody.create(nkBuilder.contentType, "")
                }
            }
            else -> RequestBody.create(NetkitBuilder.MEDIA_TYPE_PLAIN, "")
        }
        else -> null
    })

    nkBuilder

    val request = okBuilder.build()

    request.headers().toMultimap().forEach {
        println("${it.key} : ${it.value}")
    }

    println(request.url())

    return NetKitRequest()
}

fun request(block: NetkitFuncBuilder.() -> Unit): NetKitController {
    val request = NetkitFuncBuilder()
    request.block()

    val controller = NetKitController()
    controller.request = buildNetkitRequest(request)

    NetKitReactor.enqueue(controller)

    return controller
}

val executors = Executors.newFixedThreadPool(3)

//fun Any.request(block: NetkitFuncBuilder.() -> Unit) {
//    NetkitFuncBuilder().block()
//}

fun main(args: Array<String>) {

    request {
        url = "http://www.baidu.com"
        query["name"] = "123"
        query["age"] = 18
        method = "GET"

        autoStart = false
        asynchronized = false

        header = {
            Accept = "xml"
            Accept = "html"
        }

        success = key("success1") + UI + {

        }

        success = {

        }

        finish = executors + { res: String ->

        }

        after_convert = Default + {

        }

        success = UI + stringConvert {

        }

        error = Thread + {

        }
    }

    request {
        url = "baidu.com"
        success = stringConvert {

        }
    }
}
