package com.okfunc.netkit.core

import com.okfunc.netkit.error.NetkitForbidException
import kotlin.reflect.KProperty

class NetKitFunctions {
    val functions = mutableListOf<Pair<String, Function<*>>>()

    operator fun getValue(target: Any, property: KProperty<*>): Function<*> {
        throw NetkitForbidException("The get() method is not allowed to be invoked on ${property.name} feild")
    }

    operator fun setValue(target: Any, property: KProperty<*>, function: Function<*>) {
        functions += property.name to (function as? WrapFunction ?: when (property.name) {
            "before_success" -> WrapFunction(function, ThreadMode(ThreadMode.Default))
            else -> WrapFunction(function, ThreadMode(ThreadMode.UI))
        })
    }
}