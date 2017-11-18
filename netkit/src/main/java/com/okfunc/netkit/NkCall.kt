package com.okfunc.netkit

import com.okfunc.netkit.request.NkRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.reflect.KFunction
import kotlin.reflect.full.starProjectedType

/**
 *
 * Created by buck on 2017/11/10.
 */

class NkCall(val req: NkRequest<Any>) : Callback {

    val okClient = NetKit.okHttpClient()
    var okCall: Call
    var okRequest: Request
    val bundle = NkBundle()

    init {
        okRequest = req.buildOkRequest()
        okCall = okClient.newCall(okRequest)
        bundle.okCall(okCall)
    }

    fun start() {
        onStart()
        okCall.enqueue(this)
    }

    fun onAllFailure(call: Call, ex: Throwable) {
        onError(ex)
    }

    override fun onFailure(call: Call, e: IOException) = onAllFailure(call, e)

    override fun onResponse(call: Call, response: Response) = try {
        bundle.okRespone(response)
        onSuccess(req.convert.convertResponse(response, bundle), NkResponse.formOkResponse(response))
    } catch (ex: Throwable) {
        onAllFailure(call, ex)
    }

    fun cancel() {

    }

    protected fun onStart() {
        val ignore = NkIgnore()
        req.eachFunc(NkRequest.K_START) {
            if (it is KFunction<*>) callFunc(it, req, ignore)
            else (it as? NK_START<*>)?.invoke(req, ignore)
            if (ignore.ignore) return
        }
        req.callbacks.forEach { it.onStart(req) }
    }

    protected fun onFinish() {
        val ignore = NkIgnore()
        req.eachFunc(NkRequest.K_FINISH) {
            if (it is KFunction<*>) callFunc(it, req, ignore)
            else (it as? NK_FINISH<*>)?.invoke(req, ignore)
            if (ignore.ignore) return
        }
        req.callbacks.forEach { it.onFinish(req) }
    }

    protected fun onError(ex: Throwable) {
        val ignore = NkIgnore()
        req.eachFunc(NkRequest.K_ERROR) {
            if (it is KFunction<*>) callFunc(it, ex, req, ignore)
            else (it as? NK_ERROR<*>)?.invoke(ex, req, ignore)
            if (ignore.ignore) return
        }
        req.callbacks.forEach { it.onError(ex, req) }
    }

    protected fun onSuccess(result: Any, res: NkResponse) {
        val ignore = NkIgnore()
        req.eachFunc(NkRequest.K_SUCCESS) {
            if (it is KFunction<*>) callFunc(it, result, bundle, req, res, ignore)
            else (it as? NK_SUCCESS<Any>)?.invoke(result, bundle, req, res, ignore)
            if (ignore.ignore) return
        }
        req.callbacks.forEach { it.onSuccess(result, bundle, req, res) }
    }

    protected fun callFunc(func: KFunction<*>, vararg args: Any) {
        val cargs = ArrayList<Any?>()

        func.parameters.forEach { fp ->
            args.forEach p@ {
                if (it::class.starProjectedType == fp.type) {
                    cargs.add(it)
                    return@p
                }
            }
        }

        val arrrr = Array<Any?>(cargs.size) { cargs[it] }

        func.call(*arrrr)
    }
}