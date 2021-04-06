package com.rocketinsights.android.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.repos.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MessagesViewModelTest {
    // bypasses the main thread check, and immediately runs any tasks on your test thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)
    private val repo = mock<MessageRepository>()
    private val observer = mock<Observer<List<Message>>>()
    private val id = 1L
    private val text = "Done!"
    private val timestamp = 1234567L

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun shouldReturnLoadingStateInitially() = testCoroutineScope.runBlockingTest {
        // arrange
        whenever(repo.getMessages()).thenReturn(emptyFlow())

        // act
        val viewModel = MessagesViewModel(repo)

        // assert
        assertEquals(MessagesState.Loading, viewModel.messagesState.value)
    }

    @Test
    fun shouldReturnSuccessStateWhenRefreshSuccessful() = testCoroutineScope.runBlockingTest {
        // arrange
        whenever(repo.getMessages()).thenReturn(emptyFlow())

        // act
        val viewModel = MessagesViewModel(repo)

        // assert
        delay(2100)
        verify(repo).refreshMessages()
        assertEquals(MessagesState.Success, viewModel.messagesState.value)
    }

    @Test
    fun shouldReturnErrorStateWhenRefreshFails() = testCoroutineScope.runBlockingTest {
        // arrange
        val error = RuntimeException()
        whenever(repo.getMessages()).thenReturn(emptyFlow())
        whenever(repo.refreshMessages()).thenThrow(error)

        // act
        val viewModel = MessagesViewModel(repo)

        // assert
        delay(2100)
        verify(repo).refreshMessages()
        assertEquals(MessagesState.Error(error), viewModel.messagesState.value)
    }

    @Test
    fun shouldMessagesObservableBeInactiveWhileNoSubscribers() = testCoroutineScope.runBlockingTest {
        // arrange
        val messages = listOf(Message(id = id, text = text, timestamp = timestamp))
        whenever(repo.getMessages()).thenReturn(flow { emit(messages) })

        // act
        val viewModel = MessagesViewModel(repo)

        // assert
        verify(repo).getMessages()
        assertNotNull(viewModel.messages)
        assertNull(viewModel.messages.value)
    }

    @Test
    fun shouldGetListOfMessagesWhileObservableHasSubscribers() = testCoroutineScope.runBlockingTest {
        // arrange
        val messages = listOf(Message(id = id, text = text, timestamp = timestamp))
        whenever(repo.getMessages()).thenReturn(flow { emit(messages) })

        // act
        val viewModel = MessagesViewModel(repo)
        viewModel.messages.observeForever(observer)

        // assert
        verify(repo).getMessages()
        assertNotNull(viewModel.messages)
        assertEquals(messages, viewModel.messages.value)
        viewModel.messages.removeObserver(observer)
    }
}