package com.nii.aoyouvpn.core.net.convert

import com.alibaba.fastjson.JSON
import com.okfunc.netkit.NkBundle
import com.okfunc.netkit.NkIgnore
import com.okfunc.netkit.convert.NkConvert
import com.okfunc.netkit.convert.StringConvert
import okhttp3.Response
import kotlin.reflect.KClass

/**
 * Created by buck on 2017/10/24.
 */

open class ArrayConvert<T : Any>(val kcls: KClass<T>) : NkConvert<List<T>> {
    override fun convertResponse(response: Response, bundle: NkBundle, ignore: NkIgnore): List<T> {
        val json = StringConvert().convertResponse(response, bundle, ignore)
        return JSON.parseArray(json, kcls.java)
    }
}