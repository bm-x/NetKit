package com.okfunc.netkit.convert

import com.okfunc.netkit.ExtraInfo
import okhttp3.Response

/**
 * Created by buck on 2017/11/2.
 */
interface NetKitConvert<T> {
    fun convertResponse(response: Response, extraInfo: ExtraInfo): T?
}