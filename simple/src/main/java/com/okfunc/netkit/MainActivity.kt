package com.okfunc.netkit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.okfunc.netkit.convert.StringConvert
import com.okfunc.netkit.request.NkRequest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}

class aaaa : NkRequest<String>(StringConvert()) {

    fun aaa() {

    }
}
