package com.okfunc.netkit.convert

import com.okfunc.netkit.NkBundle
import com.okfunc.netkit.NkCall
import com.okfunc.netkit.NkIgnore
import okhttp3.Response

/**
 * Created by buck on 2017/11/2.
 */
open class StringConvert : NkConvert<String> {
    override fun convertResponse(response: Response, bundle: NkBundle, call:NkCall): String {
        val data = response.body()?.string()
        bundle.originData(data)
        return data!!
    }
}