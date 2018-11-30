package com.okfunc.netkit.core.utils

interface IVirtualMap<K, V> {
    operator fun get(key: K): V?
    operator fun set(key: K, value: V)
    operator fun plusAssign(pair: Pair<K, V>) = set(pair.first, pair.second)
}
