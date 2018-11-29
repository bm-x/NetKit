//package com.okfunc.netkit.cache
//
//import com.okfunc.netkit.NkCall
//import okhttp3.Call
//import okhttp3.Response
//import kotlin.reflect.KClass
//
///**
// * Created by clyde on 2017/11/24.
// */
//
//interface ICachePolicy {
//    fun onStart(call: NkCall) = Unit
//
//    fun onResponse(call: Call, response: Response, block: (call: Call, response: Response) -> Unit) {
//        block(call, response)
//    }
//}
//
//enum class CachePolicy(internal var policy: KClass<out ICachePolicy>) {
//    NONE(NonePolicy::class),
//    STREAM_DIFF(StreamDiffPolicy::class);
//
//    internal fun policy() = policy.java.newInstance()
//
//}