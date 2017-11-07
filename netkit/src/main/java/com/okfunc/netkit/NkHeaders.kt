package com.okfunc.netkit

import kotlin.collections.ArrayList

/**
 * Created by buck on 2017/10/28.
 */

open class NkHeaders {

    protected val keys = ArrayList<String>(4)
    protected val values = ArrayList<String?>(4)

    fun add(key: String, value: String) {
        keys.add(key)
        values.add(value)
    }

    fun set(key: String, value: String) {
        remove(key)
        keys.add(key)
        values.add(value)
    }

    fun addOn(key: String, value: String) {
        for (index in keys.indices) {
            if (keys[index] == key) {
                values[index] = if (values[index] == null) value else "${values[index]};$value"
                return
            }
        }

        keys.add(key)
        values.add(value)
    }

    fun remove(key: String) {
        for (index in keys.indices) {
            if (keys[index] == key) values[index] = null
        }
    }

    fun forEach(block: (key: String, value: String) -> Unit) {
        for (index in keys.indices) {
            val key = keys[index]
            val value = values[index]
            if (value != null) block(key, value)
        }
    }
}