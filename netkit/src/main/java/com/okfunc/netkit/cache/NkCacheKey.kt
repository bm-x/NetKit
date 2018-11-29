//package com.okfunc.netkit.cache
//
//import com.okfunc.netkit.UTF8
//import com.okfunc.netkit.request.NkRequest
//import java.io.File
//import java.net.URLEncoder
//
///**
// * Created by bm on 2017/11/25.
// */
//
//interface NkCacheKey {
//    fun key(req: NkRequest<*>): String
//}
//
//class DefaultCacheKeyGenerator : NkCacheKey {
//    override fun key(req: NkRequest<*>) = req.encodeFullPath
//}