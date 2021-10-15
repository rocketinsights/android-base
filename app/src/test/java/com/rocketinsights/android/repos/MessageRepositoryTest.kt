package com.rocketinsights.android.repos

import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.db.dao.MessageDao
import com.rocketinsights.android.db.models.DbMessage
import com.rocketinsights.android.mappings.toDbMessages
import com.rocketinsights.android.mappings.toMessages
import com.rocketinsights.android.network.ApiService
import com.rocketinsights.android.network.models.ApiMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class MessageRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)
    private val api = mock<ApiService>()
    private val dao = mock<MessageDao>()
    private val dispatcher = mock<DispatcherProvider>()
    private val repo by lazy { MessageRepository(api, dao, dispatcher) }
    private val id = 1L
    private val uuid = "52DF0E5BF51C428BB2EDB664DFAB4D72"
    private val text = "Done!"
    private val timestamp = 1234567L

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun shouldGetSingleMessage() = testCoroutineScope.runBlockingTest {
        // arrange
        whenever(dispatcher.io()).thenReturn(testDispatcher)
        whenever(api.getMessage()).thenReturn(ApiMessage(text = text, timestamp = timestamp))

        // act
        val message = repo.getMessage()

        // assert
        verify(api).getMessage()
        verify(dispatcher).io()
        assertEquals(text, message.text)
        assertEquals(timestamp, message.timestamp)
    }

    @Test
    fun shouldGetAndSaveListOfMessages() = testCoroutineScope.runBlockingTest {
        // arrange
        val messages = listOf(ApiMessage(text = text, timestamp = timestamp))
        whenever(dispatcher.io()).thenReturn(testDispatcher)
        whenever(api.getMessages()).thenReturn(messages)

        // act
        repo.refreshMessages()

        // assert
        verify(api).getMessages()
        verify(dao).insertMessages(messages.toDbMessages())
        verify(dispatcher).io()
    }

    @Test
    fun shouldReturnFlowOfMessages() = testCoroutineScope.runBlockingTest {
        // arrange
        val messages = listOf(DbMessage(id = id, uuid = uuid, text = text, timestamp = timestamp))
        val testFlow = flow { emit(messages) }
        whenever(dispatcher.io()).thenReturn(testDispatcher)
        whenever(dao.getMessages()).thenReturn(testFlow)

        // act
        val flow = repo.getMessages()

        // assert
        verify(dao).getMessages()
        verify(dispatcher).io()
        val expectedFlow = testFlow.map { it.toMessages() }
        assertEquals(expectedFlow.count(), flow.count())
        assertEquals(expectedFlow.first(), flow.first())
    }
}
