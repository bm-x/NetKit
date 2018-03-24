package com.okfunc.netkit.net.error

import com.okfunc.netkit.NkBundle
import com.okfunc.netkit.request.NkRequest

/**
 * Created by clyde on 2017/12/5.
 */

class ErrorHandler<T> : DefaultErrorHandler<T>() {

    override fun onError(error: Throwable, bundle: NkBundle, req: NkRequest<T>) {

    }
}