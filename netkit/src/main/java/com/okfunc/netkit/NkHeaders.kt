package com.okfunc.netkit

import okhttp3.Request
import kotlin.collections.ArrayList

/**
 * Created by buck on 2017/10/28.
 */

open class NkHeaders(initCapacity: Int = 4) {

    protected val keys = ArrayList<String>(initCapacity)
    protected val values = ArrayList<String?>(initCapacity)

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

    fun isEmpty() = keys.isEmpty() || values.isEmpty()

    fun clear() {
        keys.clear()
        values.clear()
    }

    fun copyTo(another: NkHeaders) = forEach { key, value ->
        another.keys.add(key)
        another.values.add(value)
    }

    fun copyTo(builder: Request.Builder) = forEach { key, value ->
        builder.header(key, value)
    }
}