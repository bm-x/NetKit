package com.okfunc.netkit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.okfunc.netkit.request.NkRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            val x = this::abc
            Log.i("buck", "${x.parameters}");
        }
    }

    var user: UserBean? = null
        set(value) {
            // after login
        }

    var bundle: NkBundle? = null
        set(value) {
            // set bundle
        }

    fun abc(name: String, age: Int) {

        // get https://www.api.com/example/get?key1=value1&key2=value2
        get("https://www.api.com/example/get").stringConvert()
                .params("key1", "value1")
                .params("key2", "value2")
                .onSuccess { _, _, _, _, _ -> }
                .end()

        post("https://www.api.com/example/login").objectConvert<UserBean>()
                .success(::user::set)
                .success(::bundle::set)
                .end()

        post("https://www.api.com/example/login").objectConvert<UserBean>()
                .errorHandler()
                .loadingDialog()
                .multipart("userName", "zhangsan")
                .multipart("pwd", "123456")
                .onSuccess { userInstance, _, _, _, ignore ->
                    ignore.ignore()
                    toast("登入成功  user is : $userInstance")
                }
                .onError { _, _, _, _ ->

                }
                .end()

        post("https://www.api.com/example/login").objectConvert<UserBean>()
                .success(::afterLoginSuccess)
                .error(::handleError)
                .end()
    }

    fun handleError(error: Throwable) {
        toast("error -> $error")
    }

    fun afterLoginSuccess(user: UserBean, req: NkRequest<UserBean>) {
        toast("登入成功  user is : $user")
    }
}


