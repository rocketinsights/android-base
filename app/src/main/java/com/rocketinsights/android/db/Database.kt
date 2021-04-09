package com.rocketinsights.android.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rocketinsights.android.db.Database.Companion.VERSION
import com.rocketinsights.android.db.dao.MessageDao
import com.rocketinsights.android.db.models.DbMessage

@Database(
    entities = [DbMessage::class],
    version = VERSION,
    exportSchema = false
)
abstract class Database : RoomDatabase() {

    companion object {
        const val NAME = "database"
        const val VERSION = 1
    }

    abstract fun messageDao(): MessageDao
}
