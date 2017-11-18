package com.okfunc.netkit.convert

import com.okfunc.netkit.NkBundle
import okhttp3.Response

/**
 * Created by buck on 2017/11/2.
 */
class StringConvert : NkConvert<String> {
    override fun convertResponse(response: Response, bundle: NkBundle): String {
        val data = response.body()?.string()
        bundle.originData(data)
        return data!!
    }
}