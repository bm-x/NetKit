//package com.okfunc.netkit
//
//import android.app.Application
//import android.os.Environment
//import com.okfunc.netkit.cache.CachePolicy
//import com.okfunc.netkit.cache.ICachePolicy
//import java.io.File
//
///**
// * Created by buck on 2017/11/8.
// */
//class NkConfig() {
//
//    constructor(initBlock: NkConfig.() -> Unit) : this() {
//        initBlock()
//    }
//
//    var application: Application? = null
//
//    val header = NkHeaders()
//    val params = HashMap<String, Any>()
//    var cachePolicy: ICachePolicy = CachePolicy.NONE.policy()
//
//    var host: String? = null
//    var TAG = "netkit"
//    var httpLog = false
//    var saveToFile = false
//    var logPath: File? = null
//        get() {
//            return if (saveToFile && field == null) File(Environment.getExternalStorageDirectory(), "httplog.txt")
//            else field
//        }
//
//    fun application(app: Application) = also { application = app }
//
//    fun cachePilicy(policy: CachePolicy) = also { cachePolicy = policy.policy() }
//
//    fun cachePilicy(policy: ICachePolicy) = also { cachePolicy = policy }
//
//    fun host(host: String?) = also { this.host = host }
//
//    fun header(key: String, value: String?) = also { header.set(key, value) }
//
//    fun addHeader(key: String, value: String) = also { header.add(key, value) }
//
//    fun addOnHeader(key: String, value: String) = also { header.addOn(key, value) }
//
//    fun tag(tag: String) = also { TAG = tag }
//
//    fun httpLog(enable: Boolean) = also { httpLog = enable }
//
//    fun cacheMode() {
//
//    }
//
////    fun copyTo(request: NkRequest<*>) {
////        request.header(header)
////    }
//}