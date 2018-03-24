package com.nii.aoyouvpn.core.net.convert

import com.alibaba.fastjson.JSON
import com.okfunc.netkit.NkBundle
import com.okfunc.netkit.NkIgnore
import com.okfunc.netkit.convert.NkConvert
import com.okfunc.netkit.convert.StringConvert
import kotlin.reflect.KClass

/**
 * Created by clyde on 2017/12/4.
 */

open class ObjectConvert<T : Any>(val kcls: KClass<T>) : NkConvert<T> {
    override fun convertResponse(response: okhttp3.Response, bundle: NkBundle, ignore: NkIgnore): T {
        val json = StringConvert().convertResponse(response, bundle, ignore)
        return JSON.parseObject(json, kcls.javaObjectType)
    }

}