package com.rocketinsights.android.repos

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rocketinsights.android.coroutines.DispatcherProvider
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MessageRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)
    private val api = mock<ApiService>()
    private val dispatcher = mock<DispatcherProvider>()
    private val repo by lazy { MessageRepository(api, dispatcher) }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun getMessage() = testCoroutineScope.runBlockingTest {
        // arrange
        whenever(dispatcher.io()).thenReturn(testDispatcher)
        whenever(api.getMessage()).thenReturn(Message("Done!"))

        // act
        val message = repo.getMessage()

        // assert
        verify(api).getMessage()
        assertEquals("Done!", message.text)
    }
}