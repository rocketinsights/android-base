package com.rocketinsights.kotlinmvp.views

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rocketinsights.kotlinmvp.R
import com.rocketinsights.kotlinmvp.presenters.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    private val presenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun setText(text: String) {
        message.text = text
    }

    override fun getContext(): Context = this

}
