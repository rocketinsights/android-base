package com.rocketinsights.android.models

import android.os.Parcel
import android.os.Parcelable

data class Contact(
    val id: Long,
    val name: String,
    val phoneNumber: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(phoneNumber)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact = Contact(parcel)

        override fun newArray(size: Int): Array<Contact?> = arrayOfNulls(size)
    }
}
