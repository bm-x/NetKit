package com.okfunc.netkit.convert

import com.okfunc.netkit.ExtraInfo
import okhttp3.Response
import kotlin.reflect.KFunction

/**
 * Created by buck on 2017/11/2.
 */
class StringConvert : NetKitConvert<String> {
    override fun convertResponse(response: Response, extraInfo: ExtraInfo): String? {
        val data = response.body()?.string()
        extraInfo.originData(data)
        return data
    }
}