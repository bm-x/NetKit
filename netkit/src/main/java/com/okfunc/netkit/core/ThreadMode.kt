package com.okfunc.netkit.core

import java.util.concurrent.Executor

class ThreadMode(
        val mode: Int,
        var executor: Executor? = null
) {
    companion object {
        internal val Default = 1
        internal val UI = 2
        internal val Thread = 3
        internal val Executor = 4
    }

    operator fun plus(target: Function<*>) = if (target is WrapFunction) {
        target.threadMode(this)
    } else {
        WrapFunction(target, this)
    }

    operator fun plus(target: CharSequence) = WrapFunction(threadMode = this, name = target.toString())

    operator fun plus(target: Executor): ThreadMode {
        return ThreadMode(ThreadMode.Executor, target)
    }
}