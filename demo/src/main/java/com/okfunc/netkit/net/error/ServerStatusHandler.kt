package com.okfunc.netkit.net.error

import com.okfunc.netkit.NkBundle
import com.okfunc.netkit.NkCallback
import com.okfunc.netkit.request.NkRequest

/**
 * Created by clyde on 2018/1/13.
 */

class ServerStatusHandler<T> : NkCallback<T> {
    override fun onError(error: Throwable, bundle: NkBundle, req: NkRequest<T>) {
    }
}