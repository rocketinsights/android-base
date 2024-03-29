package com.rocketinsights.android.ui.components

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * DialogDsl is an example of how to create components using Kotlin DSL. The main advantage of this
 * is the reduction of the boilerplate code, and therefore improves the readability of our code.
 *
 * These components make sense when it is actually used in many parts of our code and/or it is a
 * very customizable component.
 */
@DslMarker
annotation class DslDialog

@DslDialog
class DialogBuilder(
    val context: Context,
    val setup: DialogBuilder.() -> Unit = {}
) {

    @StringRes
    var titleRes: Int? = null
    var title: String? = null

    @StringRes
    var contentRes: Int? = null
    var content: String? = null

    @StringRes
    var positiveTextRes: Int? = null
    var positiveText: String? = null

    @StringRes
    var negativeTextRes: Int? = null
    var negativeText: String? = null

    var customLayout: View? = null

    var positiveAction: (() -> Unit)? = null
    var negativeAction: (() -> Unit)? = null
    var dismissAction: (() -> Unit)? = null

    var cancelable: Boolean = true

    fun build(): AlertDialog {
        setup()

        return MaterialAlertDialogBuilder(context).apply {
            quartus(title, { setTitle(it) }, titleRes, { setTitle(it) })
            quartus(content, { setMessage(it) }, contentRes, { setMessage(it) })

            customLayout?.let {
                setView(it)
            }

            negativeAction?.let { action ->
                quartus(
                    negativeText, { setNegativeButton(it) { _, _ -> action.invoke() } },
                    negativeTextRes, { setNegativeButton(it) { _, _ -> action.invoke() } }
                )
            }

            positiveAction?.let { action ->
                quartus(
                    positiveText, { setPositiveButton(it) { _, _ -> action.invoke() } },
                    positiveTextRes, { setPositiveButton(it) { _, _ -> action.invoke() } }
                )
            }

            dismissAction?.let {
                setOnDismissListener {
                    dismissAction?.invoke()
                }
            }

            setCancelable(cancelable)
        }.create()
    }
}

private fun <T, U> Any.quartus(
    primaryValue: T?,
    primaryAction: ((T) -> Unit)? = null,
    secondaryValue: U?,
    secondaryAction: ((U) -> Unit)? = null
) {
    primaryValue?.let {
        primaryAction?.invoke(it)
    } ?: run {
        secondaryValue?.let {
            secondaryAction?.invoke(it)
        }
    }
}
