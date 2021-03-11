package com.rocketinsights.android.managers

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.livinglifetechway.quickpermissions_kotlin.util.QuickPermissionsOptions
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PermissionsManager(
    private val context: Context
) {
    open class Exception : RuntimeException() {
        class PermissionsDenied(deniedPermissions: List<String>) : Exception()
        class PermissionPermanentlyDeniedException(deniedPermissions: List<String>) : Exception()
    }

    fun hasPermissions(vararg permissions: String): Boolean {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(context, it) != PERMISSION_GRANTED) {
                return false
            }
        }

        return true
    }

    suspend fun requestPermissions(activity: AppCompatActivity, vararg permissions: String): Unit =
        suspendCoroutine { cont ->
            activity.runWithPermissions(*permissions,
                options = permissionsRequestOptions(cont)) {
                cont.resume(Unit)
            }
        }

    suspend fun requestPermissions(fragment: Fragment, vararg permissions: String): Unit =
        suspendCoroutine { cont ->
            fragment.runWithPermissions(*permissions,
                options = permissionsRequestOptions(cont)) {
                cont.resume(Unit)
            }
        }

    private fun permissionsRequestOptions(cont: Continuation<Unit>) = QuickPermissionsOptions(
        handleRationale = false,
        handlePermanentlyDenied = true,
        permanentDeniedMethod = {
            cont.resumeWithException(Exception.PermissionPermanentlyDeniedException(
                it.deniedPermissions.toList()
            ))
        },
        permissionsDeniedMethod = {
            cont.resumeWithException(Exception.PermissionsDenied(it.deniedPermissions.toList()))
        }
    )
}