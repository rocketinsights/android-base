package com.rocketinsights.android.auth

import android.net.Uri

data class AuthUser(
    val uid: String = "",
    val providerId: String = "",
    val displayName: String = "",
    val photoUrl: Uri? = null,
    val email: String = "",
    val phoneNumber: String = ""
)
