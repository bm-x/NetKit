package com.okfunc.netkit

import android.util.Log
import okhttp3.*
import okio.Buffer
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.Charset

/**
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

        val sendTxt = "Sending request ${request.url()}\n${request.headers()}\n${reqBody(request)}"

        val response = try {
            chain.proceed(request)
        } catch (ex: Throwable) {
            Log.e(TAG, writeToFile("${sendTxt}\n\nrequest ${request.url()} error: $ex\n-------------------------------------\n"))
            throw ex
        }

        var res = response
        val body = if (isPlainText(response.body()?.contentType())) {
            val bytes = response.body()?.bytes()
            val type = response.body()?.contentType()
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
        Log.i(TAG, writeToFile("${sendTxt}\n\nReceived response for ${response.request().url()} in ${((t2 - t1) / 1e6).toInt()}ms\n${response.headers()}\nbody : $body\n-------------------------------------\n"))
        return res
    }

    fun writeToFile(txt: String): String {
        if (saveToFile) {
            synchronized(lock) {
                OutputStreamWriter(FileOutputStream(logpath, true), "utf-8").use {
                    it.write(txt)
                }
            }
        }
        return txt
    }

    fun reqBody(req: Request): String {
        if (isPlainText(req.body()?.contentType())) {
            val body = req.body() ?: return "body : request body is null"
            val buffer = Buffer()
            body.writeTo(buffer)
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
    }

}