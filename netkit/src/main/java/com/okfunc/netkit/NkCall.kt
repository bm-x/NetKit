package com.okfunc.netkit

import com.okfunc.netkit.request.IRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * Created by buck on 2017/11/10.
 */

class NkCall(val request: IRequest<*>) : Callback {

    val okClient = NetKit.okHttpClient()
    var okCall: Call
    var okRequest: Request

    init {
        okRequest = request.buildOkRequest()
        okCall = okClient.newCall(okRequest)
    }

    fun start() {
        okCall.enqueue(this)
    }


    fun onAllFailure(call: Call, e: Throwable) {

    }

    override fun onFailure(call: Call, e: IOException) = onAllFailure(call, e)

    override fun onResponse(call: Call, response: Response) = try {
        response.body()
    } catch (ex: Throwable) {
        onAllFailure(call, ex)
    }

    fun cancel() {

    }

}