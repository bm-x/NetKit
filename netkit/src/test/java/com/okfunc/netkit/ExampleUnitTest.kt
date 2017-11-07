package com.okfunc.netkit

import okhttp3.*
import org.junit.Test
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction4

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun netkit1() {

    }

    fun call2(name: KFunction1<ExampleUnitTest, Unit>) {

    }

    fun call(name: KFunction4<String, Int, List<String>, HashMap<String, String>, Unit>) {

    }

    fun abc(block: (String, Int, List<String>, HashMap<String, String>) -> Unit) {

    }

    fun text(name: String, age: Int, list: List<String>, map: HashMap<String, String>) {
        println("test fun")
    }

    @Test
    fun writeObjectToFile() {
        val list = mutableListOf<String>()
        list.add("测试")
        list.add("你好")
        list.add("测试")

        list.removeIf { it == "测试" }

        println(list)
    }

    @Test
    fun addition_isCorrect() {
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val request = Request.Builder()
                .url("http://www.baidu.com/")
                .header("demo", "zhangsan")
                .header("demo", "lisi")
                .addHeader("demo", "zhangsan")
                .build()

        client.newCall(request).execute().body()?.close()
    }

    val interceptor: (chain: Interceptor.Chain) -> Response = { chain ->
        val request = chain.request()

        val t1 = System.nanoTime()
        println(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()))

        val response = chain.proceed(request)

        val t2 = System.nanoTime()
        println(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6, response.headers()))

        response
    }
}