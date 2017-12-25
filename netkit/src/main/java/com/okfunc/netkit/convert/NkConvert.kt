package com.okfunc.netkit.convert

import com.okfunc.netkit.NkBundle
import com.okfunc.netkit.NkIgnore
import okhttp3.Response

/**
 * Created by buck on 2017/11/2.
 */
interface NkConvert<T> {
    fun convertResponse(response: Response, bundle: NkBundle, ignore: NkIgnore): T
}