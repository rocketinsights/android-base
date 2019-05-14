package com.rocketinsights.android.viewmodels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rocketinsights.android.models.Message
import org.junit.Assert
import org.junit.Test
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.doReturn
import com.rocketinsights.android.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val app = mock<Application> {
        on { getString(R.string.loading) } doReturn "Loading…"
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
            on { getMessageAsync() } doReturn GlobalScope.async { Message("Done!") }
        })

        Thread.sleep(2100)
        Assert.assertEquals(Message("Done!"), viewModel.message.value)
    }
}