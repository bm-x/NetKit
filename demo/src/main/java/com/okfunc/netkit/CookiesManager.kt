package com.okfunc.netkit

import com.alibaba.fastjson.JSON
import com.okfunc.core.base.application
import com.okfunc.core.base.ext.clearDirectory
import com.okfunc.core.base.ext.elazy
import com.okfunc.core.base.ext.safety
import com.okfunc.core.base.md5_16
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.io.File

class CookiesManager : CookieJar {
    companion object {
        private val lock = Any()
        private val directory by elazy { File(application.filesDir, "cookies") }

        fun clearCookies() = safety {
            directory.clearDirectory()
        }
    }

    private val cookiesMap = HashMap<String, MutableList<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        val host = url.host()
        val cl = cookiesMap[host]
        if (cl != null) {
            cookiesMap.remove(host)
        }
        cookiesMap[host] = cookies

        synchronized(lock) {
            file(md5_16(host).toString() to cookies.map { SimpleCookies.fromOkCookies(it) })
        }
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        val host = url.host()
        var cookies: MutableList<Cookie>? = cookiesMap[host]
        if (cookies == null) {
            cookies = fileList<SimpleCookies>(md5_16(host).toString()).map { it.toOkCookie() }.toMutableList()
            cookiesMap[host] = cookies
        }
        return cookies
    }

    fun file(vararg pairs: Pair<String, Any?>) = pairs.forEach {
        safety {
            if (it.second == null) {
                File(directory, it.first).delete()
            } else {
                File(directory, it.first).writeText(it.second as? String
                        ?: JSON.toJSONString(it.second))
            }
        }
    }

    private inline fun <reified T> fileList(key: String): List<T> = try {
        val list = fileString(key, "")
        JSON.parseArray(list, T::class.java)
    } catch (e: Throwable) {
        e.printStackTrace()
        emptyList()
    }

    private fun fileString(key: String, def: String? = null): String? {
        val file = File(directory, key)
        if (!file.exists()) return def
        return try {
            val txt = file.readText()
            if (txt.isEmpty()) def else txt
        } catch (e: Throwable) {
            def
        }
    }

}

