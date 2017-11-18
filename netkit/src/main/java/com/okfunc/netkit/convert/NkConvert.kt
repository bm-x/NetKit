package com.okfunc.netkit.convert

import com.okfunc.netkit.NkBundle
import okhttp3.Response

/**
 * Created by buck on 2017/11/2.
 */
interface NkConvert<T> {
    fun convertResponse(response: Response, bundle: NkBundle): T
}