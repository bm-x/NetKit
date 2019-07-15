package com.okfunc.netkit

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.okfunc.netkit.bean.Record
import com.okfunc.netkit.bean.ResponeWrap
import com.okfunc.netkit.request.NkRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetKit.globalConfig.httpLog = true

        btn.setOnClickListener {
            get("https://github.com/").stringConvert()
                    .onSuccess { target, bundle, req, res, ignore ->

                    }
                    .end()

//            try {
//                // val file = getExternalFilesDir(null)
//                val file = File(Environment.getExternalStorageDirectory(), "Android")
//                val okfunc = File(file, "okfunc")
//                okfunc.mkdirs()
//                File(okfunc, "${packageName}.debug").writeText("true")
//                Log.i("clyde", file.absolutePath)
//            } catch (e: Throwable) {
//                Log.e("clyde", "", e)
//            }

//            post("http://okfunc.com/record/r/show").objectConvert<ResponeWrap<List<Record>>>()
////                    .onSuccess { target, bundle, req, res, ignore ->
////                        Log.d("clyde", "success $target")
////                    }
//                    .success { target: ResponeWrap<List<Record>> ->
//                        Log.d("clyde", "code ${target.code}   msg ${target.msg}  ${target.page.size}")
//                    }
//                    .onFinish { req, ignore ->
//                        Log.d("clyde", "finish")
//                    }
//                    .end()
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


