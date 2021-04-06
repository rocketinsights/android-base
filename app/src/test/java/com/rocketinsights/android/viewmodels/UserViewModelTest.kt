package com.rocketinsights.android.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rocketinsights.android.auth.AuthUser
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.repos.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class UserViewModelTest {
    // bypasses the main thread check, and immediately runs any tasks on your test thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)
    private val repo = mock<AuthRepository>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun shouldNotifyUserIsNotLoggedIn() = testCoroutineScope.runBlockingTest {
        // arrange
        val observer = mock<Observer<Boolean>>()
        whenever(repo.observeUser()).thenReturn(flow { emit(null) })

        // act
        val viewModel = UserViewModel(repo)
        viewModel.isLoggedIn.observeForever(observer)

        // assert
        assertEquals(false, viewModel.isLoggedIn.value)
        viewModel.isLoggedIn.removeObserver(observer)
    }

    @Test
    fun shouldNotifyUserIsLoggedIn() = testCoroutineScope.runBlockingTest {
        // arrange
        val observer = mock<Observer<Boolean>>()
        whenever(repo.observeUser()).thenReturn(flow { emit(AuthUser()) })

        // act
        val viewModel = UserViewModel(repo)
        viewModel.isLoggedIn.observeForever(observer)

        // assert
        assertEquals(true, viewModel.isLoggedIn.value)
        viewModel.isLoggedIn.removeObserver(observer)
    }
}