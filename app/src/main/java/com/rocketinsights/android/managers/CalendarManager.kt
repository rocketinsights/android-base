package com.rocketinsights.android.managers

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import com.rocketinsights.android.prefs.LocalStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.Year
import java.time.ZonedDateTime
import java.util.Calendar

/**
 * CalendarManager allows us to add events to the current user's default Calendar through a ContentProvider.
 *
 * There are several things to keep in mind:
 * - In obtaining the events, it is necessary to pass a range of dates, which is now hardcoded a
 * range between two years less and more than the current one.
 * - There is another simpler way to manipulate the calendar through Intents, but these make the user
 * leave the current application to end the action in the device's calendar app. In addition, if we
 * use Intents it is not necessary to request read and write permissions for the calendar.
 *
 * More information about Calendar Provider:
 * https://developer.android.com/guide/topics/providers/calendar-provider
 */
interface CalendarManager {
    companion object {
        val CALENDAR_PERMISSIONS = arrayOf(
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
        )
    }

    suspend fun addEvent(
        title: String,
        description: String,
        startDate: ZonedDateTime,
        endDate: ZonedDateTime,
        address: String
    ): Long

    fun events(): Flow<List<CalendarEvent>>

    suspend fun refreshEvents()
}

data class CalendarEvent(val id: Long, val title: String, val description: String)

class CalendarManagerImpl(
    private val context: Context,
    private val localStore: LocalStore
) : CalendarManager {
    private val savedEventsIds = localStore.getStringSetValue(CALENDAR_EVENTS_IDS)

    private val events = MutableStateFlow<List<CalendarEvent>>(listOf())

    companion object {
        const val CALENDAR_EVENTS_IDS = "CALENDAR_EVENTS_IDS"
    }

    override suspend fun addEvent(
        title: String,
        description: String,
        startDate: ZonedDateTime,
        endDate: ZonedDateTime,
        address: String
    ): Long {
        val startMillis = startDate.toInstant().toEpochMilli()
        val endMillis = endDate.toInstant().toEpochMilli()

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.CALENDAR_ID, getDefaultCalendarId())
            put(CalendarContract.Events.EVENT_TIMEZONE, startDate.zone.id)
        }

        val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

        // Get the event ID that is the last element in the Uri
        val eventId = uri?.lastPathSegment?.toLong() ?: -1

        if (eventId != -1L) {
            // Update Saved Events Info
            var savedEvents = (
                savedEventsIds.firstOrNull()?.toMutableSet()
                    ?: mutableSetOf()
                ).apply {
                add(eventId.toString())
            }

            localStore.setStringSetValue(CALENDAR_EVENTS_IDS, savedEvents)

            // Refresh Events
            retrieveEvents()
        }

        return eventId
    }

    override fun events(): Flow<List<CalendarEvent>> = events

    override suspend fun refreshEvents() {
        retrieveEvents()
    }

    private suspend fun retrieveEvents() {
        val instanceProjection: Array<String> = arrayOf(
            CalendarContract.Instances.EVENT_ID, // 0
            CalendarContract.Instances.BEGIN, // 1
            CalendarContract.Instances.TITLE, // 2
            CalendarContract.Instances.DESCRIPTION // 3
        )

        // The indices for the projection array above.
        val projectionIdIndex = 0
        val projectionBeginIndex = 1
        val projectionTitleIndex = 2
        val projectionDescriptionIndex = 3

        // Specify the date range you want to search for recurring
        // event instances
        // TODO: Update this according to the App Requirements
        val startMillis: Long = Calendar.getInstance().run {
            set(Year.now().value - 2, 1, 1, 0, 0)
            timeInMillis
        }
        val endMillis: Long = Calendar.getInstance().run {
            set(Year.now().value + 2, 1, 1, 0, 0)
            timeInMillis
        }

        // Get Saved Events
        var savedEvents = savedEventsIds.firstOrNull()?.joinToString(separator = ",") ?: ""

        val selection = "${CalendarContract.Instances.CALENDAR_ID} = ${getDefaultCalendarId()} AND " +
            "${CalendarContract.Instances.EVENT_ID} IN ($savedEvents)"

        // Construct the query with the desired date range.
        val builder: Uri.Builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, startMillis)
        ContentUris.appendId(builder, endMillis)

        // Submit the query
        val cur: Cursor? = context.contentResolver.query(
            builder.build(),
            instanceProjection,
            selection,
            null,
            null
        )

        val updatedEvents = mutableListOf<CalendarEvent>()

        while (cur != null && cur.moveToNext()) {
            // Get the field values
            val id: Long = cur.getLong(projectionIdIndex)
            val beginVal: Long = cur.getLong(projectionBeginIndex)
            val title: String = cur.getString(projectionTitleIndex)
            val description: String = cur.getString(projectionDescriptionIndex)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = beginVal
            }
            val formatter = SimpleDateFormat("MM/dd/yyyy")
            Timber.d("Calendar Event Found with Date: ${formatter.format(calendar.time)}")

            updatedEvents.add(CalendarEvent(id = id, title = title, description = description))
        }

        events.emit(updatedEvents)
    }

    private fun getDefaultCalendarId(): Long? {
        val projection = arrayOf(CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)

        var calendarCursor = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.IS_PRIMARY + " = 1",
            null,
            CalendarContract.Calendars._ID + " ASC"
        )

        if (calendarCursor != null && calendarCursor.count <= 0) {
            calendarCursor = context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                CalendarContract.Calendars.VISIBLE + " = 1",
                null,
                CalendarContract.Calendars._ID + " ASC"
            )
        }

        return if (calendarCursor != null &&
            calendarCursor.moveToFirst()
        ) {
            val calendarName: String
            val calendarID: String
            val nameCol = calendarCursor.getColumnIndex(projection[1])
            val idCol = calendarCursor.getColumnIndex(projection[0])

            calendarName = calendarCursor.getString(nameCol)
            calendarID = calendarCursor.getString(idCol)

            calendarCursor.close()

            return calendarID.toLong()
        } else {
            null
        }
    }
}
