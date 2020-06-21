package dev.gitly

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import dev.gitly.core.prefs.KThemes
import dev.gitly.core.prefs.ThemePrefs
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject


/**
 * [Application] entry point
 */
@HiltAndroidApp
class GitlyApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var themePrefs: ThemePrefs

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        // Test debugger
        debugger("Application instance created successfully")

        // Observe current theme
        themePrefs.liveTheme.observeForever { themeKey ->
            debugger("Current theme -> ${themeKey.name}")
            when (themeKey) {
                KThemes.LIGHT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                KThemes.DARK -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                KThemes.FOLLOW_SYSTEM -> {
                    AppCompatDelegate.setDefaultNightMode(
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
                            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                        else
                            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    )
                }

                else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}