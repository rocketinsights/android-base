package com.rocketinsights.android.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "message",
    indices = [Index("uuid", unique = true)]
)
data class DbMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(defaultValue = "") val uuid: String = "",
    @ColumnInfo(defaultValue = "") val text: String = "",
    @ColumnInfo(defaultValue = "") val timestamp: Long = System.currentTimeMillis()
)
