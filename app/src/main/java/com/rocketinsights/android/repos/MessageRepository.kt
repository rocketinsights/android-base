package com.rocketinsights.android.repos

import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.db.dao.MessageDao
import com.rocketinsights.android.mappings.toDbMessages
import com.rocketinsights.android.mappings.toMessage
import com.rocketinsights.android.mappings.toMessages
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MessageRepository(
    private val api: ApiService,
    private val messageDao: MessageDao,
    private val dispatcher: DispatcherProvider
) {

    suspend fun getMessage(): Message =
        withContext(dispatcher.io()) { api.getMessage().toMessage() }

    /**
     * Fetch messages from the remote API and save them to local DB.
     */
    suspend fun refreshMessages() {
        withContext(dispatcher.io()) {
            val messages = api.getMessages().toDbMessages()
            messageDao.insertMessages(messages)
        }
    }

    fun getMessages(): Flow<List<Message>> =
        messageDao.getMessages()
            .distinctUntilChanged()
            .map { it.toMessages() }
            .flowOn(dispatcher.io())
}