package com.rocketinsights.android.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val id: Long,
    val name: String,
    val phoneNumber: String
) : Parcelable
