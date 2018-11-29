package com.okfunc.netkit.core.utils

open class VirtualPairMap<K, V>(initialCapacity: Int = 5) : IVirtualMap<K, V> {

    private val list = ArrayList<Pair<K, V>>(initialCapacity)

    fun isEmpty() = list.isEmpty()

    override fun get(key: K): V? = list.firstOrNull { it.first == key }?.second

    override fun set(key: K, value: V) {
        list += key to value
    }

    fun getAll(key: K) = list.filter { it.first == key }

    fun foreach(block: (Pair<K, V>) -> Unit) = list.forEach(block)
}