package com.okfunc.netkit.request

import com.okfunc.netkit.convert.NetKitConvert

/**
 * Created by buck on 2017/10/28.
 */
open class NkGetRequest<T>(impl: Request<NkRequest<T>>) : Request<NkRequest<T>> by impl {


}