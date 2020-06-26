package com.owllife.youtubebookmark.core

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
    }
}