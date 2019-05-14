package com.rocketinsights.android.viewmodels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rocketinsights.android.models.Message
import org.junit.Assert
import org.junit.Test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.rocketinsights.android.R
import com.rocketinsights.android.ext.toDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOError
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val app = mock<Application> {
        on { getString(R.string.loading) } doReturn "Loading…"
        on { getString(R.string.http_error) } doReturn "Http Error"
        on { getString(R.string.unknown_error) } doReturn "Unknown Error"
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
    }

    @Test
    fun getMessage() {
        val viewModel = MainViewModel(app, mock())
        Assert.assertNotNull(viewModel.message)
        Assert.assertEquals(Message("Loading…"), viewModel.message.value)
    }

    @Test
    fun getDelayedMessage() {
        val viewModel = MainViewModel(app, mock {
            on { getMessageAsync() } doReturn Message("Done!").toDeferred()
        })

        // don't do this normally, we just have an artificial delay in our view model
        Thread.sleep(2100)
        Assert.assertEquals(Message("Done!"), viewModel.message.value)
    }

    @Test
    fun getDelayedMessageHttpError() {
        val errorResponse: Response<Message> = Response.error(500, ResponseBody.create(
                MediaType.parse("application/json"), ""))
        val viewModel = MainViewModel(app, mock {
            on { getMessageAsync() } doThrow HttpException(errorResponse)
        })

        // don't do this normally, we just have an artificial delay in our view model
        Thread.sleep(2100)
        Assert.assertEquals(Message("Http Error"), viewModel.message.value)
    }

    @Test
    fun getDelayedMessageUnknownError() {
        val viewModel = MainViewModel(app, mock {
            on { getMessageAsync() } doThrow IOError(Throwable())
        })

        // don't do this normally, we just have an artificial delay in our view model
        Thread.sleep(2100)
        Assert.assertEquals(Message("Unknown Error"), viewModel.message.value)
    }
}