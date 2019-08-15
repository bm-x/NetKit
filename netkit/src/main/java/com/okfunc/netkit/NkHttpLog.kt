package com.okfunc.netkit

import android.util.Log
import okhttp3.*
import okio.Buffer
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset
import kotlin.math.ceil

/**
 *
 * NkHttpLog
 *
 * Created by clyde on 2017/11/18.
 */
class NkHttpLog(val saveToFile: Boolean = false, val logpath: File? = null) : Interceptor {

    var TAG: String

    init {
        TAG = NetKit.globalConfig.TAG
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val t1 = System.nanoTime()
        val url = request.url()
        val okHttpClient = NetKit.okHttpClient()
        val cookies = okHttpClient.cookieJar().loadForRequest(url)
        val cookieString = if (cookies.isEmpty()) {
            ""
        } else {
            "Cookies : " + cookies.map { "${it}\n" }.joinToString("")
        }

        val sendTxt = "Sending request [${request.method()}] ${url}\n${request.headers()}${cookieString}${reqBody(request)}"

        val response = try {
            chain.proceed(request)
        } catch (ex: Throwable) {
            log("${sendTxt}\n\nrequest ${request.url()} error: $ex\n-------------------------------------\n")
            throw ex
        }

        var res = response
        var contentLength = 0
        val body = if (isPlainText(response.body()?.contentType())) {
            val bytes = response.body()?.bytes()
            val type = response.body()?.contentType()
            contentLength = bytes?.size ?: -1
            res = response.newBuilder().body(ResponseBody.create(type, bytes
                    ?: ByteArray(0))).build()
            if (bytes == null) {
                "respone body is null"
            } else {
                try {
                    JSONObject(String(bytes)).toString()
                } catch (e: Throwable) {
                    String(bytes)
                }
            }
        } else "maybe [binary body] or empty, omitted!"

        val t2 = System.nanoTime()
        log("${sendTxt}\n\nReceived response for ${response.request().url()} in ${((t2 - t1) / 1e6).toInt()}ms \n${response.headers()}Content-Length : ${contentLength}\nbody : \n")
        log("$body\n-------------------------------------\n")
        return res
    }

    private fun log(logTxt: String) {
        writeToFile(logTxt)
        printLog(logTxt)
    }

    private fun printLog(log: String) {
        if (log.length < 1000) {
            Log.i(TAG, log)
        } else {
            for (i in 0 until (ceil(log.length / 1000f).toInt())) {
                val start = i * 1000
                var end = start + 1000
                if (end > log.length) end = log.length
                Log.i(TAG, log.substring(start, end))
            }
        }
    }

    fun writeToFile(txt: String) {
        if (saveToFile) {
            synchronized(lock) {
                OutputStreamWriter(FileOutputStream(logpath, true), "utf-8").use {
                    it.write(txt)
                }
            }
        }
    }

    fun reqBody(req: Request): String {
        if (isPlainText(req.body()?.contentType())) {
            val body = req.body() ?: return "body : request body is null"
            val buffer = Buffer()
            if (body is MultipartBody && body.size() != 0) {
                body.parts().forEach {
                    buffer.write(body.boundary().toByteArray())
                    buffer.write(NEW_LINE)

                    buffer.write(it.headers().toString().toByteArray())
                    buffer.write(NEW_LINE)

                    if (it.body().contentType() == MEDIA_TYPE_STREAM) {
                        buffer.write("maybe [binary body] or empty, omitted!\n".toByteArray())
                    } else {
                        it.body().writeTo(buffer)
                        buffer.write(NEW_LINE)
                    }
                }
                buffer.write(body.boundary().toByteArray())
            } else {
                body.writeTo(buffer)
            }
            return "body : ${buffer.readString(readCharset(req.body()?.contentType()))}"
        } else return "body : maybe [binary body] or empty, omitted!"
    }

    fun readCharset(mt: MediaType?) = (mt?.charset(UTF8) ?: UTF8) ?: UTF8

    fun isPlainText(mt: MediaType?) = when (mt?.type()?.toLowerCase()) {
        "text" -> true
        "multipart" -> true
        else -> when (mt?.subtype()?.toLowerCase()) {
            "json", "xml", "html", "x-www-form-urlencoded", "form-data" -> true
            else -> false
        }
    }

    private val UTF8 = Charset.forName("UTF-8")

    companion object {
        private val lock = ""
        private val NEW_LINE = "\n".toByteArray()
    }

}