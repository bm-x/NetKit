package com.okfunc.netkit

import com.okfunc.netkit.request.NkRequest

/**
 * Created by clyde on 2017/11/24.
 */

object NkController {

    val queue = ArrayList<NkCall>(15)

    fun enqueue(req: NkRequest<Any>) {
        synchronized(queue) {
            val call = NkCall(req)
            queue.add(call)
            call.start()
        }
    }

    fun cancel(tag: Any) {
        synchronized(queue) {
            queue.removeIft(false, { it.req.tag == tag }, { it.cancel() })
        }
    }

    fun remove(req: NkRequest<Any>) {
        synchronized(queue) {
            queue.removeIft(true) { it.req == req }
        }
    }

    fun cancelAll(){
        queue.forEach { it.cancel() }
        queue.clear()
    }
}