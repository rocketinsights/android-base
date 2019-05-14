package com.rocketinsights.android.repos

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.rocketinsights.android.ext.toDeferred
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Test

import org.junit.Before
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class MessageRepositoryTest {
    private val api = mock<ApiService> {
        on { getMessageAsync() } doReturn Message("Done!").toDeferred()
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
    }

    @Test
    fun getMessageAsync() {
        val repo = MessageRepository(api)
        Assert.assertEquals("Done!", repo.getMessageAsync().getCompleted().text)
    }
}