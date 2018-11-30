package com.okfunc.netkit.core.builder

import com.okfunc.netkit.MEDIA_TYPE_FORM
import com.okfunc.netkit.MEDIA_TYPE_FORM_URLENCODED
import com.okfunc.netkit.MEDIA_TYPE_PLAIN
import com.okfunc.netkit.MEDIA_TYPE_STREAM
import com.okfunc.netkit.convert.stringConvert
import com.okfunc.netkit.core.*
import com.okfunc.netkit.core.utils.VirtualPairMap
import okhttp3.*
import okio.ByteString
import java.io.File
import java.nio.charset.Charset
import java.util.concurrent.Executors

abstract class NetkitBuilder {
    abstract var url: String?
    abstract var protocol: String?
    abstract var host: String?
    abstract var port: Int?
    abstract var path: String?
    abstract var method: String?

    abstract var content: Any?
    abstract var contentType: MediaType?

    abstract val query: VirtualPairMap<Any, Any?>

    abstract val params: VirtualPairMap<Any, Any?>

    abstract var autoStart: Boolean?
    abstract var asynchronized: Boolean?

    internal abstract fun finish()
    internal abstract fun header(): NetKitHeader?
    internal abstract fun functions(): NetKitFunctions
    internal abstract fun config(): NetkitConfig?
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

    nkBuilder.header()?.entrys?.forEach {
        okBuilder.addHeader(it.first, it.second ?: "")
    }

    nkBuilder.query.foreach {
        urlBuilder.addQueryParameter(it.first.toString(), it.second?.toString())
    }

    okBuilder.url(urlBuilder.build())

    okBuilder.method(nkBuilder.method ?: "GET", when (nkBuilder.method) {
        "POST" -> when {
            nkBuilder.content is RequestBody -> {
                nkBuilder.content as RequestBody
            }
            nkBuilder.contentType == MEDIA_TYPE_FORM_URLENCODED -> {
                val fb = FormBody.Builder(Charset.defaultCharset());
                nkBuilder.params.foreach {
                    fb.add(it.first.toString(), it.second?.toString() ?: "")
                }
                fb.build()
            }
            nkBuilder.contentType == MEDIA_TYPE_FORM -> {
                val fb = MultipartBody.Builder()
                nkBuilder.params.foreach {
                    val second = it.second
                    if (second is File) fb.addFormDataPart(it.first.toString(), second.name, RequestBody.create(MEDIA_TYPE_STREAM, second))
                    else fb.addFormDataPart(it.first.toString(), second?.toString() ?: "")
                }
                fb.build()
            }
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
            else -> RequestBody.create(MEDIA_TYPE_PLAIN, "")
        }
        else -> null
    })

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
