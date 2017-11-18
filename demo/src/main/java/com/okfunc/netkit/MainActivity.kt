package com.okfunc.netkit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            val x = this::abc
            Log.i("buck", "${x.parameters}");
        }
    }

    fun abc(name: String, age: Int) {

    }
}
