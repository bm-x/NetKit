package com.okfunc.netkit.core

interface IVirtualMap<K, V> {
    operator fun get(key: K): V
    operator fun set(key: K, value: V)
}
