package com.okfunc.netkit.cache

import com.okfunc.netkit.NetKit
import com.okfunc.netkit.NkCall
import okhttp3.Call
import okhttp3.Response
import java.io.File

/**
 *
 * Created by clyde on 2017/11/24.
 */
class StreamDiffPolicy : ICachePolicy {

    var directory: File? = null

    init {
        NetKit.globalConfig.application?.let {
            directory = File(it.cacheDir, this::class.simpleName)
            directory?.mkdirs()
        }
    }

    override fun onStart(call: NkCall) {
        val dir = directory ?: return

    }

    override fun onResponse(call: Call, response: Response, block: (call: Call, response: Response) -> Unit) {
        val dir = directory ?: return block(call, response)

    }
}
