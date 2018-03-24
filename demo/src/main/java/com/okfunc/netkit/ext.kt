package com.okfunc.netkit

import com.nii.aoyouvpn.core.net.convert.ArrayConvert
import com.nii.aoyouvpn.core.net.convert.ObjectConvert
import com.okfunc.netkit.net.NetLoadingState
import com.okfunc.netkit.net.ResponeBean
import com.okfunc.netkit.net.error.DefaultErrorHandler
import com.okfunc.netkit.net.error.ErrorHandler
import com.okfunc.netkit.net.error.ServerStatusHandler
import com.okfunc.netkit.request.NkRequest
import kotlin.collections.set

/**
 * Created by clyde on 2017/12/4.
 */

fun NkBundle.responeBean(bean: ResponeBean) {
    info["responeBean"] = bean
}

fun isEmpty(txt: String?) = txt == null || txt.isEmpty()
fun notEmpty(txt: String?) = txt != null && txt.isNotEmpty()

fun NkBundle.responeBean() = info["responeBean"] as ResponeBean?

fun <T> NkRequest<T>.loadingDialog(cancelable: Boolean = true, msg: String? = null, cancelBlock: (() -> Unit)? = null) = callback(NetLoadingState<T>(cancelable, msg, cancelBlock))

fun <T> NkRequest<T>.serverStatusHandler() = callback(ServerStatusHandler())
fun <T> NkRequest<T>.errorHandler() = callback(ErrorHandler())
fun <T> NkRequest<T>.defaultErrorHandler() = callback(DefaultErrorHandler())

inline fun <reified T : Any> NetKitUrl.objectConvert() = convert(ObjectConvert(T::class))
inline fun <reified T : Any> NetKitUrl.arrayConvert() = convert(ArrayConvert(T::class))

