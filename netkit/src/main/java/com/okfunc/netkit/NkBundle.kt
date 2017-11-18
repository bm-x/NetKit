package com.okfunc.netkit

import okhttp3.Call
import okhttp3.Response

/**
 * Created by buck on 2017/11/2.
 */
class NkBundle(val info: HashMap<String, Any?> = HashMap()) {

    fun originData(data: Any?) {
        info[ORIGIN_DATA] = data
    }

    fun originData() = info[ORIGIN_DATA]

    fun okCall(call: Call) {
        info[OK_CALL] = call
    }

    fun okCall() = info[OK_CALL] as Call

    fun okRespone(res: Response) {
        info[OK_RESPONSE] = res
    }

    fun okRespone() = info[OK_RESPONSE] as? Response

    companion object {
        const val ORIGIN_DATA = "origin_data"
        const val OK_CALL = "ok_call"
        const val OK_RESPONSE = "ok_response"
    }
}