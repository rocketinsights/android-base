package com.rocketinsights.android.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.rocketinsights.android.auth.AuthUserLiveData
import com.rocketinsights.android.repos.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthViewModelTest {
    // bypasses the main thread check, and immediately runs any tasks on your test thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)
    private val repo = mock<AuthRepository>()
    private val user = mock<AuthUserLiveData>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun shouldAddIdTokenListener() = testCoroutineScope.runBlockingTest {
        // act
        AuthViewModel(repo, user)

        // assert
        verify(repo).addIdTokenListener()
    }
}