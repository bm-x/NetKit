package com.okfunc.netkit

/**
 * Created by buck on 2017/11/2.
 */
class ExtraInfo(val info: HashMap<String, Any?> = HashMap()) {

    fun originData(data: Any?) {
        info[ORIGIN_DATA] = data
    }

    fun originData() = info[ORIGIN_DATA]

    companion object {
        const val ORIGIN_DATA = "origin_data"
    }
}