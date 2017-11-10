package com.okfunc.netkit.request

import android.util.Log

/**
 * Created by buck on 2017/10/28.
 */
open class NkPostRequest<T>
internal constructor(internal var base: IRequest<NkRequest<T>>)
    : IRequest<NkRequest<T>> by base {

    override fun clone(base: IRequest<NkRequest<T>>) = NkPostRequest<T>(base)

    override fun clone() = clone(base)
}