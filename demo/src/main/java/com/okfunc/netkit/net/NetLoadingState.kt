package com.okfunc.netkit.net

import com.okfunc.netkit.NkBundle
import com.okfunc.netkit.NkCallback
import com.okfunc.netkit.request.NkRequest

/**
 * Created by clyde on 2017/12/5.
 */

class NetLoadingState<T>(
        val cancelable: Boolean = true,
        val msg: String? = null,
        val cancelBlock: (() -> Unit)? = null) : NkCallback<T> {


    override fun onFinish(req: NkRequest<T>) {
    }

    override fun onError(error: Throwable, bundle: NkBundle, req: NkRequest<T>) {
    }

    override fun onStart(req: NkRequest<T>) {
    }

    companion object {
        const val DEFAULT_MSG = "loading..."
    }
}