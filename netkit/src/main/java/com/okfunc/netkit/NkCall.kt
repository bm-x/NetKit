package com.okfunc.netkit

import android.util.Log
import com.okfunc.netkit.cache.ICachePolicy
import com.okfunc.netkit.request.NkRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.reflect.KFunction
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.javaType

/**
 *
 * Created by buck on 2017/11/10.
 */

class NkCall(val req: NkRequest<Any>) : Callback {

    val okClient = NetKit.okHttpClient()
    var okCall: Call
    var okRequest: Request
    val bundle = NkBundle()
    val cachePolicy: ICachePolicy = req.cachePolicy ?: NetKit.globalConfig.cachePolicy

    var block = false

    init {
        okRequest = req.buildOkRequest()
        okCall = okClient.newCall(okRequest)
        bundle.okCall(okCall)
    }

    fun start() {
        onStart()
        cachePolicy.onStart(this)
        okCall.enqueue(this)
    }

    fun onAllFailure(call: Call, ex: Throwable) {
        ex.printStackTrace()
        ui(false) { onError(ex) }
        ui(false) { onFinish() }
    }

    override fun onFailure(call: Call, e: IOException) = onAllFailure(call, e)

    override fun onResponse(c: Call, res: Response) {
        try {
            cachePolicy.onResponse(c, res) { call, response ->
                bundle.okRespone(response)
                val result = req.convert.convertResponse(response, bundle, this)
                ui {
                    onSuccess(result, NkResponse.formOkResponse(call, response))
                    onFinish()
                }
            }
        } catch (ex: Throwable) {
            onAllFailure(c, ex)
        }
    }

    fun ui(responeFail: Boolean = true, block: () -> Unit) {
        uihandler.post {
            try {
                block()
            } catch (ex: Throwable) {
                if (responeFail) onAllFailure(okCall, ex)
            }
        }
    }

    fun cancel() {
        block = true
        okCall.cancel()
    }

    fun onStart() {
        val ignore = NkIgnore()
        req.eachFunc(NkRequest.K_START) {
            if (it is KFunction<*>) callFunc(it, req, ignore)
            else (it as? NK_START<*>)?.invoke(req, ignore)
            if (ignore.ignore) return
        }
        req.callbacks.forEach { it.onStart(req) }
    }

    fun onFinish() {
        val ignore = NkIgnore()
        req.eachFunc(NkRequest.K_FINISH) {
            if (it is KFunction<*>) callFunc(it, req, ignore)
            else (it as? NK_FINISH<*>)?.invoke(req, ignore)
            if (ignore.ignore) return
        }
        req.callbacks.forEach { it.onFinish(req) }
    }

    fun onError(ex: Throwable) {
        val ignore = NkIgnore()
        req.eachFunc(NkRequest.K_ERROR) {
            if (it is KFunction<*>) callFunc(it, ex, bundle, req, ignore)
            else (it as? NK_ERROR<*>)?.invoke(ex, bundle, req, ignore)
            if (ignore.ignore) return
        }
        req.callbacks.forEach { it.onError(ex, bundle, req) }
    }

    fun onSuccess(result: Any, res: NkResponse) {
        val ignore = NkIgnore()
        req.eachFunc(NkRequest.K_SUCCESS) {
            if (it is KFunction<*>) callFunc(it, result, bundle, req, res, ignore)
            else (it as? NK_SUCCESS<Any>)?.invoke(result, bundle, req, res, ignore)
            if (ignore.ignore) return
        }
        req.callbacks.forEach { it.onSuccess(result, bundle, req, res) }
    }

    fun callFunc(func: KFunction<*>, vararg args: Any) {
        val list = ArrayList<Any?>()
        func.parameters.forEachIndexed { index, fp ->
            list.add(null)
            args.forEach p@ {
                val t = it::class.starProjectedType
                if (t == fp.type || t.isSubtypeOf(fp.type) || t.classifier == fp.type.classifier) {
                    list[index] = it
                    return@p
                }
            }
        }

        func.call(*Array(list.size) { list[it] })
    }
}