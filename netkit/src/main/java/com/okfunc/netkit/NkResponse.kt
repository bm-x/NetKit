package com.okfunc.netkit

import okhttp3.Call
import okhttp3.Response

/**
 * Created by bm on 2017/11/15.
 */

class NkResponse {

    companion object {
        internal fun formOkResponse(call: Call, res: Response): NkResponse {
            return NkResponse()
        }
    }
}