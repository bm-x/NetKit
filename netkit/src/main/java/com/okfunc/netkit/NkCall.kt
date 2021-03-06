//package com.okfunc.netkit
//
//import com.okfunc.netkit.cache.ICachePolicy
//import com.okfunc.netkit.request.NkRequest
//import okhttp3.Call
//import okhttp3.Callback
//import okhttp3.Request
//import okhttp3.Response
//import java.io.IOException
//import java.lang.reflect.ParameterizedType
//
///**
// *
// * Created by buck on 2017/11/10.
// */
//
//class NkCall(val req: NkRequest<Any>) : Callback {
//
//    val okClient = NetKit.okHttpClient()
//    var okCall: Call
//    var okRequest: Request
//    val bundle = NkBundle()
//    val cachePolicy: ICachePolicy = req.cachePolicy ?: NetKit.globalConfig.cachePolicy
//
//    init {
//        okRequest = req.buildOkRequest()
//        okCall = okClient.newCall(okRequest)
//        bundle.okCall(okCall)
//    }
//
//    fun start() {
//        onStart()
//        cachePolicy.onStart(this)
//        okCall.enqueue(this)
//    }
//
//    fun onAllFailure(call: Call, ex: Throwable) {
//        ex.printStackTrace()
//        postui { onError(ex) }
//        postui { onFinish() }
//    }
//
//    override fun onFailure(call: Call, e: IOException) = onAllFailure(call, e)
//
//    override fun onResponse(c: Call, res: Response) = try {
//        cachePolicy.onResponse(c, res) policy@{ call, response ->
//            bundle.okRespone(response)
//            val ignore = NkIgnore()
//            val result = req.convert.convertResponse(response, bundle, ignore)
//            if (!ignore.ignore && !c.isCanceled) {
//                beforeSuccess(result, NkResponse.formOkResponse(call, response))
//                postui { onSuccess(result, NkResponse.formOkResponse(call, response)) }
//                postui { onFinish() }
//            }
//        }
//    } catch (ex: Throwable) {
//        onAllFailure(c, ex)
//    }
//
//    fun postui(block: () -> Unit) {
//        ui {
//            try {
//                block()
//            } catch (ex: Throwable) {
//                onAllFailure(okCall, ex)
//            }
//        }
//    }
//
//    fun cancel() {
//        okCall.cancel()
//    }
//
//    fun onStart() {
//        val ignore = NkIgnore()
//        req.eachFunc(NkRequest.K_START) {
//            callFunc(it, arrayOf(req, ignore))
//            if (ignore.ignore) return
//        }
//        req.callbacks.forEach { it.onStart(req) }
//    }
//
//    fun onFinish() {
//        val ignore = NkIgnore()
//        req.eachFunc(NkRequest.K_FINISH) {
//            callFunc(it, arrayOf(req, ignore))
//            if (ignore.ignore) return
//        }
//        req.callbacks.forEach { it.onFinish(req) }
//    }
//
//    fun onError(ex: Throwable) {
//        val ignore = NkIgnore()
//        req.eachFunc(NkRequest.K_ERROR) {
//            callFunc(it, arrayOf(ex, bundle, req, ignore))
//            if (ignore.ignore) return
//        }
//        req.callbacks.forEach { it.onError(ex, bundle, req) }
//    }
//
//    fun beforeSuccess(result: Any, res: NkResponse) {
//        val ignore = NkIgnore()
//        req.eachFunc(NkRequest.K_BEFORE_SUCCESS) {
//            callFunc(it, arrayOf(result, bundle, req, res, ignore))
//            if (ignore.ignore) return
//        }
//        req.callbacks.forEach { it.beforeSuccess(result, bundle, req, res) }
//    }
//
//    fun onSuccess(result: Any, res: NkResponse) {
//        val ignore = NkIgnore()
//        req.eachFunc(NkRequest.K_SUCCESS) {
//            callFunc(it, arrayOf(result, bundle, req, res, ignore))
//            if (ignore.ignore) return
//        }
//        req.callbacks.forEach { it.onSuccess(result, bundle, req, res) }
//    }
//
//    fun callFunc(func: Function<*>, fullArgs: Array<*>) {
//        val cls = func.javaClass
//        val fullTypes = Array<Class<*>?>(fullArgs.size) { fullArgs[it]?.javaClass }
//
//        try {
//            val method = cls.getDeclaredMethod("invoke", *fullTypes)
//            method.invoke(func, fullArgs)
//        } catch (e: Throwable) {
//            matchCall(cls, func, fullTypes, fullArgs)
//        }
//    }
//
//    private fun matchCall(cls: Class<*>, func: Function<*>, fullCall: Array<Class<*>?>, fullArgs: Array<*>) {
//        val method = cls.declaredMethods.filter { !it.isBridge && it.name == "invoke" }.firstOrNull()
//                ?: return
//
//        val paramsTypes = method.genericParameterTypes
//
//        if (paramsTypes.isEmpty()) {
//            method.invoke(func)
//            return
//        }
//
//        val params = arrayOfNulls<Any?>(paramsTypes.size)
//
//        paramsTypes.forEachIndexed { index, paramsType ->
//            val clz: Class<*> = if (paramsType is ParameterizedType) {
//                paramsType.rawType as Class<*>
//            } else {
//                paramsType as Class<*>
//            }
//
//            params[index] = fullArgs.find { clz.isInstance(it) }
//        }
//
//        method.invoke(func, *params)
//    }
//}