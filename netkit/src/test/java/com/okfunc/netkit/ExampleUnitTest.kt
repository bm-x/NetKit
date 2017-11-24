package com.okfunc.netkit

import android.util.Log
import com.okfunc.netkit.request.NkRequest
import okhttp3.*
import okio.Buffer
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.Charset
import kotlin.reflect.KFunction
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
        NetKit.globalConfig = NkConfig().httpLog(true)
        NetKit.post("http://www.baidu.com/").stringConvert()
                .json("--------------")
                .onSuccess { target, bundle, req, res, ignore ->
                    Log.i("clyde", "onSuccess")
                }.end()

        Thread.sleep(3000)
    }

    fun headerParpre(bundle: NkBundle, ignore: NkIgnore) {
        val cc = bundle.okRespone()?.header("Cache-Control")
        if (cc != null && cc.isNotEmpty()) {
            ignore.ignore()
            println("has header Cache-Control:${cc}  ignore other success func")
        }
        println("headerParpre")
    }

    protected fun callFunc(func: KFunction<*>, vararg args: Any) {
        println(func)
    }

    fun xxxx(target: String, bundle: NkBundle, ignore: NkIgnore, req: NkRequest<String>, res: NkResponse) {

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
        val client = OkHttpClient.Builder().build()

        val request = Request.Builder()
                .url("http://www.baidu.com/")
                .header("demo", "zhangsan")
                .header("demo", "lisi")
                .addHeader("demo", "zhangsan")
                .post(RequestBody.create(MEDIA_TYPE_PLAIN, "test------------"))
                .build()

        val buf1 = Buffer()
        request.body()?.writeTo(buf1)

        val buf2 = Buffer()
        request.body()?.writeTo(buf2)

        println(buf1.readString(Charset.forName("UTF-8")))
        println(buf2.readString(Charset.forName("UTF-8")))

        //val x = client.newCall(request)

//        val out = ByteArrayOutputStream()
//        val res = client.newCall(request).execute()
//        println(out.toString())
//        println("------------------------------------")
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