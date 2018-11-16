package com.okfunc.netkit.request


class WrapFunction(
        val target: Function<*>,
        var threadMode: ThreadMode? = null
) : Function<Any>
