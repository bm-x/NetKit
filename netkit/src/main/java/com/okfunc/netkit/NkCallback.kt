package com.okfunc.netkit

/**
 * Created by buck on 2017/11/3.
 */
interface NkCallback {
    fun onStart() = Unit

    fun onFinish() = Unit

    fun onError() = Unit

    fun onSuccess() = Unit
}