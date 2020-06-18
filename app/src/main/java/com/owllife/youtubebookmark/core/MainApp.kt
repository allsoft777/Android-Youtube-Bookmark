package com.owllife.youtubebookmark.core

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setRxJavaError()
        Fabric.with(this, Crashlytics())
    }

    private fun setRxJavaError() {
        RxJavaPlugins.setErrorHandler { e: Throwable ->
//            if (e is UndeliverableException) {
//                e = e.cause!!
//            }
            if (e is IOException || e is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@setErrorHandler
            }
            if (e is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@setErrorHandler
            }
            if (e is NullPointerException || e is IllegalArgumentException) {
                // that's likely a bug in the application
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), e)
                return@setErrorHandler
            }
            if (e is IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), e)
                return@setErrorHandler
            }
        }
    }
}