package com.rocketinsights.android.ui

class ParentScrollProvider {
    private var interactor: ((Boolean) -> Unit)? = null

    fun enableTouchOnParentScrollContainer(enable: Boolean) {
        interactor?.invoke(enable)
    }

    fun registerEnablementOfParentScrollFunction(interactor: (Boolean) -> Unit) {
        this.interactor = interactor
    }
}
