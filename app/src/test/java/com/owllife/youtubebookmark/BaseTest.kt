package com.owllife.youtubebookmark

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * @author owllife.dev
 * @since 20. 7. 3
 */
@RunWith(RobolectricTestRunner::class)
@Config(
    application = BaseTest.ApplicationStub::class,
    sdk = [Build.VERSION_CODES.P]
)
abstract class BaseTest {
    fun getContext(): Context = ApplicationProvider.getApplicationContext()
    internal class ApplicationStub : Application()
}