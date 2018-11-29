package com.okfunc.netkit

import com.alibaba.fastjson.JSON
import com.okfunc.netkit.core.builder.request
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        request {
            url = "://baiud.com/aaa"
            protocol = "http"
            host = "www.baidu.com"
            port = 8080
            path = "index.html"

            query["name"] = "zhang  san-+"
            query["age"] = 18

            header["header1"] = "value1"

            method = "POST"

            json = JSON.toJSONString("")

            contentType = MEDIA_TYPE_PLAIN
        }
    }
}
