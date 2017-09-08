package com.rocketinsights.android.ui

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.rocketinsights.android.R
import com.rocketinsights.android.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : LifecycleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.message.observe(this, Observer { data ->
            message.text = data?.text
        })
    }
}
