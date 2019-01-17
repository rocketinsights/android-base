package com.rocketinsights.android.ui

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rocketinsights.android.R
import com.rocketinsights.android.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel.message.observe(this, Observer { data ->
            message.text = data?.text
        })
    }
}
