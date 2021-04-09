package com.rocketinsights.android.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.repos.MessageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
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
    private val repo = mock<MessageRepository>()
    private val id = 1L
    private val text = "Done!"
    private val timestamp = 1234567L

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun getMessage() = testCoroutineScope.runBlockingTest {
        // act
        val viewModel = MainViewModel(repo)

        // assert
        assertNotNull(viewModel.messageState)
        assertEquals(MainMessageState.Loading, viewModel.messageState.value)
    }

    @Test
    fun getDelayedMessage() = testCoroutineScope.runBlockingTest {
        // arrange
        val message = Message(id = id, text = text, timestamp = timestamp)
        whenever(repo.getMessage()).thenReturn(message)

        // act
        val viewModel = MainViewModel(repo)

        // assert
        delay(2100)
        assertEquals(MainMessageState.Success(message), viewModel.messageState.value)
    }

    @Test
    fun getDelayedMessageHttpError() = testCoroutineScope.runBlockingTest {
        // arrange
        val errorResponse: Response<Message> =
            Response.error(500, "".toResponseBody("application/json".toMediaTypeOrNull()))
        val error = HttpException(errorResponse)
        whenever(repo.getMessage()).thenThrow(error)

        // act
        val viewModel = MainViewModel(repo)

        // assert
        delay(2100)
        assertEquals(MainMessageState.Error(error), viewModel.messageState.value)
    }

    @Test
    fun getDelayedMessageUnknownError() = testCoroutineScope.runBlockingTest {
        // arrange
        val error = IOError(Throwable())
        whenever(repo.getMessage()).thenThrow(error)

        // act
        val viewModel = MainViewModel(repo)

        // assert
        delay(2100)
        assertEquals(MainMessageState.Error(error), viewModel.messageState.value)
    }
}
