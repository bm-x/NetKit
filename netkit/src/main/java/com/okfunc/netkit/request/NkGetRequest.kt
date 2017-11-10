package com.okfunc.netkit.request

/**
 * Created by buck on 2017/10/28.
 */
open class NkGetRequest<T>
internal constructor(internal var base: IRequest<NkRequest<T>>)
    : IRequest<NkRequest<T>> by base {

    override fun clone(base: IRequest<NkRequest<T>>) = NkGetRequest<T>(base)

    override fun clone() = clone(base)
}