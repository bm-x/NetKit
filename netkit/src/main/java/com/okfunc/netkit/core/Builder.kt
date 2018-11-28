package com.okfunc.netkit.core

import com.okfunc.netkit.convert.stringConvert
import java.util.concurrent.Executors
import kotlin.collections.set

val executors = Executors.newFixedThreadPool(3)

fun Any.request(block: NetKitRequest.() -> Unit) {
    NetKitRequest().block()
}

fun request(block: NetKitRequest.() -> Unit): NetKitController {
    val request = NetKitRequest()
    request.block()

    val controller = NetKitController()
    controller.request = request

    NetKitReactor.enqueue(controller)

    return controller
}

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
