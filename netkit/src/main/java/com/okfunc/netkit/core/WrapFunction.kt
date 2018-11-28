package com.okfunc.netkit.core


class WrapFunction(
        var target: Function<*>? = null,
        var threadMode: ThreadMode? = null,
        var name: String? = null
) : Function<Any?> {

    fun threadMode(tm: ThreadMode): WrapFunction {
        threadMode = tm
        return this
    }

    fun name(n: String?): WrapFunction {
        name = n
        return this
    }

    operator fun plus(target: Function<*>) = also {
        this.target = target
    }
}

