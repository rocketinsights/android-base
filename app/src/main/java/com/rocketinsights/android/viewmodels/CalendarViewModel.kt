package com.rocketinsights.android.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rocketinsights.android.extensions.setEvent
import com.rocketinsights.android.managers.CalendarEvent
import com.rocketinsights.android.managers.CalendarManager
import com.rocketinsights.android.viewmodels.event.Event
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.ZonedDateTime

class CalendarViewModel(
    private val calendarManager: CalendarManager
) : ViewModel() {
    companion object {
        val CALENDAR_PERMISSIONS = CalendarManager.CALENDAR_PERMISSIONS
    }

    private val _calendarState = MutableLiveData<Event<CalendarState>>()
    val calendarState: LiveData<Event<CalendarState>> get() = _calendarState

    val events: LiveData<List<CalendarEvent>> = calendarManager.events().asLiveData()

    fun refreshEvents() {
        _calendarState.setEvent(CalendarState.Loading)
        viewModelScope.launch {
            try {
                calendarManager.refreshEvents()
                _calendarState.setEvent(CalendarState.Success)
            } catch (error: Throwable) {
                _calendarState.setEvent(CalendarState.Error(error))
                Timber.e(error)
            }
        }
    }

    fun addEvent() {
        _calendarState.setEvent(CalendarState.Loading)
        viewModelScope.launch {
            try {
                calendarManager.addEvent(
                    "Rocket Insights Event",
                    getRandomString(20),
                    ZonedDateTime.now().apply {
                        plusMinutes(15)
                    },
                    ZonedDateTime.now().apply {
                        plusMinutes(30)
                    },
                    "Event Address"
                )
                _calendarState.setEvent(CalendarState.Success)
            } catch (error: Throwable) {
                _calendarState.setEvent(CalendarState.Error(error))
                Timber.e(error)
            }
        }
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}

sealed class CalendarState {
    object Loading : CalendarState()
    object Success : CalendarState()
    data class Error(val exception: Throwable) : CalendarState()
}
