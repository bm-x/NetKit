//package com.okfunc.netkit
//
//import com.okfunc.netkit.request.NkRequest
//
///**
// * Created by buck on 2017/11/3.
// */
//interface NkCallback<T> {
//    fun onStart(req: NkRequest<T>) = Unit
//
//    fun onFinish(req: NkRequest<T>) = Unit
//
//    fun onError(error: Throwable, bundle: NkBundle, req: NkRequest<T>) = Unit
//
//    fun onSuccess(target: T, bundle: NkBundle, req: NkRequest<T>, res: NkResponse) = Unit
//
//    fun beforeSuccess(target: T, bundle: NkBundle, req: NkRequest<T>, res: NkResponse) = Unit
//
//    fun onUploadProgress() = Unit
//
//    fun onDownloadProgress() = Unit
//}