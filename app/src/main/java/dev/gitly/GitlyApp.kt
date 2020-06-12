package dev.gitly

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

import timber.log.Timber.DebugTree


/**
 * [Application] entry point
 */
@HiltAndroidApp
class GitlyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        // Test debugger
        debugger("Application instance created successfully")
    }
}