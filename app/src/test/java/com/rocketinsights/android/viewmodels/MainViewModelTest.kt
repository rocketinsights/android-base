package com.rocketinsights.android.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.rocketinsights.android.R
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.repos.MessageRepository
import com.rocketinsights.android.ui.common.StringResProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOError

@ExperimentalCoroutinesApi
class MainViewModelTest {
    // bypasses the main thread check, and immediately runs any tasks on your test thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)
    private val stringRes = mock<StringResProvider> {
        on { getString(R.string.loading) } doReturn "Loading…"
        on { getString(R.string.http_error) } doReturn "Http Error"
        on { getString(R.string.unknown_error) } doReturn "Unknown Error"
    }
    private val repo = mock<MessageRepository>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun getMessage() = testCoroutineScope.runBlockingTest {
        // act
        val viewModel = MainViewModel(repo, stringRes)

        // assert
        assertNotNull(viewModel.message)
        assertEquals(Message("Loading…"), viewModel.message.value)
    }

    @Test
    fun getDelayedMessage() = testCoroutineScope.runBlockingTest {
        // arrange
        whenever(repo.getMessage()).thenReturn(Message("Done!"))

        // act
        val viewModel = MainViewModel(repo, stringRes)

        // assert
        delay(2100)
        assertEquals(Message("Done!"), viewModel.message.value)
    }

    @Test
    fun getDelayedMessageHttpError() = testCoroutineScope.runBlockingTest {
        // arrange
        val errorResponse: Response<Message> =
            Response.error(500, ResponseBody.create(MediaType.parse("application/json"), ""))
        whenever(repo.getMessage()).thenThrow(HttpException(errorResponse))

        // act
        val viewModel = MainViewModel(repo, stringRes)

        // assert
        delay(2100)
        assertEquals(Message("Http Error"), viewModel.message.value)
    }

    @Test
    fun getDelayedMessageUnknownError() = testCoroutineScope.runBlockingTest {
        // arrange
        whenever(repo.getMessage()).thenThrow(IOError(Throwable()))

        // act
        val viewModel = MainViewModel(repo, stringRes)

        // assert
        delay(2100)
        assertEquals(Message("Unknown Error"), viewModel.message.value)
    }
}