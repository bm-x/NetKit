package com.okfunc.netkit.core

import com.okfunc.netkit.core.utils.IVirtualMap
import kotlin.reflect.KProperty

class NetKitHeader : IVirtualMap<Any, Any?> {

    var Accept by this
    var Accept_Charset by this
    var Accept_Encoding by this
    var Accept_Language by this
    var Authorization by this
    var Connection by this
    var Content_Length by this
    var Cookie by this
    var From by this
    var Host by this
    var If_Modified_Since by this
    var Pragma by this
    var Referer by this
    var User_Agent by this

    internal val entrys = mutableListOf<Pair<String, String?>>()

    override fun set(key: Any, value: Any?) {
        entrys += key.toString() to value?.toString()
    }

    override fun get(key: Any): Any? {
        val _key = key.toString()
        val list = entrys.filter { it.first == _key }
        if (list.isEmpty()) return null
        else if (list.size == 1) return list[0].second
        else {
            val sb = StringBuilder()
            list.forEach {
                sb.append(it.second)
                sb.append(';')
            }
            sb.deleteCharAt(sb.length - 1)
            return sb.toString()
        }
    }

    private fun KProperty<*>.httpHeaderName() = name.replace('_', '-')

    operator fun getValue(target: Any, property: KProperty<*>) =
            get(property.httpHeaderName()) as? String

    operator fun setValue(target: Any, property: KProperty<*>, s: Any?) {
        set(property.httpHeaderName(), s)
    }
}