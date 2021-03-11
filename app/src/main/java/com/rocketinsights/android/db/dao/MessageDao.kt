package com.rocketinsights.android.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rocketinsights.android.db.models.DbMessage
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MessageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertMessages(moves: List<DbMessage>)

    @Query("SELECT * FROM message ORDER BY timestamp desc")
    abstract fun getMessages(): Flow<List<DbMessage>>
}
