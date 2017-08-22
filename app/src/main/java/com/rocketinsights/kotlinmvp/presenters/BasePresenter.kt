package com.rocketinsights.kotlinmvp.presenters

import com.rocketinsights.kotlinmvp.views.BaseView

/**
 * Created by brian on 8/21/17.
 */

abstract class BasePresenter<in V : BaseView> {
    protected open var view: BaseView? = null

    open fun attachView(view: V) {
        this.view = view
    }

    fun detachView() {
        view = null
    }
}