package com.okfunc.netkit.request

import java.util.concurrent.Executor

class ThreadMode(
        val mode: Int,
        var executor: Executor? = null
) {
    companion object {
        internal val UI = 1
        internal val Worker = 2
        internal val Thread = 3
        internal val Executor = 4
    }
}

operator fun ThreadMode.plus(target: Function<*>): Function<*> {
    if (target is WrapFunction) {
        target.threadMode = this
        return target
    } else {
        return WrapFunction(target, this)
    }
}

operator fun ThreadMode.plus(target: Executor): ThreadMode {
    return ThreadMode(ThreadMode.Executor, target)
}