package com.rocketinsights.android.viewmodels

import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rocketinsights.android.models.Message
import com.rocketinsights.android.ui.MainActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainViewModelTest {

    @get:Rule
    var activity: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun getMessage() {
        val viewModel = MainViewModel(activity.activity.application)
        Assert.assertNotNull(viewModel.message)
        Assert.assertEquals(Message( "Loading…"), viewModel.message.value)
    }

    @Test
    fun getDelayedMessage() {
        val viewModel = MainViewModel(activity.activity.application)

        Thread.sleep(5500)
        Assert.assertEquals(Message( "Done after five seconds!"), viewModel.message.value)
    }
}