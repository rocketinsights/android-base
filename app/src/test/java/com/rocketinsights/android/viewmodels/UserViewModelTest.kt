package com.rocketinsights.android.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rocketinsights.android.auth.AuthUser
import com.rocketinsights.android.repos.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

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
