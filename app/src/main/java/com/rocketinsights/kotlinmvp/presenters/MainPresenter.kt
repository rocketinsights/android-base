package com.rocketinsights.kotlinmvp.presenters

import android.os.Handler
import com.rocketinsights.kotlinmvp.R
import com.rocketinsights.kotlinmvp.models.Message
import com.rocketinsights.kotlinmvp.views.MainView

/**
 * Created by brian on 8/21/17.
 */
class MainPresenter : BasePresenter<MainView>() {
    override fun attachView(view: MainView) {
        super.attachView(view)

        Handler().postDelayed({
            val message = Message(this.view?.getContext()?.getString(R.string.done))

            getView()?.let {
                it.setText(message.text ?: it.getContext().getString(R.string.error))
            }
        }, 5000)
    }

    private fun getView(): MainView? {
        return if (view is MainView) {
            view as MainView
        } else {
            null
        }
    }
}