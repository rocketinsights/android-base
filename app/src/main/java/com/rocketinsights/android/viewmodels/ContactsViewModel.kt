package com.rocketinsights.android.viewmodels

import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.loader.content.Loader
import com.rocketinsights.android.models.Contact
import kotlinx.coroutines.launch

class ContactsViewModel : ViewModel() {

    private val _contactsLiveData: MutableLiveData<List<Contact>> by lazy {
        MutableLiveData<List<Contact>>()
    }
    val contactsLiveData: LiveData<List<Contact>> = _contactsLiveData

    fun getContentUri(): Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

    fun getProjection(): Array<out String> = arrayOf(
        ContactsContract.CommonDataKinds.Phone._ID,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

    fun getSelection(): String {
        return SELECTION_START +
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} NOTNULL" + AND +
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} != ''" + AND +
            "${ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER} = true" + AND +
            "${ContactsContract.CommonDataKinds.Phone.MIMETYPE} = '${ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE}'" + AND +
            "${ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID} = ${ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID}" +
            SELECTION_END
    }

    fun getSortOrder(): String = SORT_ORDER_BY_DISPLAY_NAME

    fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) = viewModelScope.launch {
        if (loader.id != CONTACTS_QUERY_ID) return@launch
        val contacts = mutableListOf<Contact>()
        while (!data.isClosed && data.moveToNext()) {
            contacts.add(
                Contact(
                    id = data.getLong(COLUMN_CONTACT_ID),
                    name = data.getString(COLUMN_CONTACT_NAME),
                    phoneNumber = data.getString(COLUMN_PHONE_NUMBER)
                )
            )
        }
        data.close()

        _contactsLiveData.value = contacts
    }

    fun onLoaderReset() {
        // no - op because we are not holding any reference to the cursor
    }

    fun getContactsQueryId(): Int = CONTACTS_QUERY_ID

    companion object {
        private const val CONTACTS_QUERY_ID = 0
        private const val COLUMN_CONTACT_ID = 0
        private const val COLUMN_CONTACT_NAME = 1
        private const val COLUMN_PHONE_NUMBER = 2
        private const val SORT_ORDER_BY_DISPLAY_NAME = "${ContactsContract.Contacts.DISPLAY_NAME} ASC"
        private const val SELECTION_START = "(("
        private const val SELECTION_END = "))"
        private const val AND = ") AND ("
    }
}
