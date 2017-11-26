package com.okfunc.netkit

import com.okfunc.netkit.request.NkRequest
import okhttp3.MediaType

/**
 * Created by bm on 2017/11/16.
 */

internal val UTF8 = "utf-8"

internal val MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8")
internal val MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8")
internal val MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream")

internal typealias NK_SUCCESS<T> = (target: T, bundle: NkBundle, req: NkRequest<T>, res: NkResponse, ignore: NkIgnore) -> Unit

internal typealias NK_START<T> = (req: NkRequest<T>, ignore: NkIgnore) -> Unit

internal typealias NK_FINISH<T> = (req: NkRequest<T>, ignore: NkIgnore) -> Unit

internal typealias NK_ERROR<T> = (error: Throwable, req: NkRequest<T>, ignore: NkIgnore) -> Unit

internal typealias NK_UPLOAD_PROGRESS<T> = (req: NkRequest<T>) -> Unit

internal typealias NK_DOWNLOAD_PROGRESS<T> = (req: NkRequest<T>) -> Unit

internal fun <T> MutableList<T>.removeIft(ifReturn: Boolean = false, block: (it: T) -> Boolean) {
    removeIft(ifReturn, block, null)
}

internal fun <T> MutableList<T>.removeIft(ifReturn: Boolean = false, block: (it: T) -> Boolean, then: ((it: T) -> Unit)? = null) {
    for (index in size - 1 downTo 0) {
        if (block(get(index))) {
            removeAt(index)
            if (then != null) then(get(index))
            if (ifReturn) return
        }
    }
}