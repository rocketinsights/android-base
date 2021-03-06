package com.rocketinsights.android

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun useAppContext() {
        // Context of the app under test.
        val appContext = ApplicationProvider.getApplicationContext<RocketApplication>()

        if (BuildConfig.FLAVOR == "dev") {
            assertEquals("com.rocketinsights.android.dev", appContext.packageName)
        } else {
            assertEquals("com.rocketinsights.android", appContext.packageName)
        }
    }
}
