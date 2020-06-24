package dev.gitly.core.prefs

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

enum class KThemes {
    LIGHT, DARK, FOLLOW_SYSTEM
}

/**
 * [SharedPreferences] implementation for themes
 */
class ThemePrefs @Inject constructor(context: Context) {
    private val prefs by lazy {
        context.getSharedPreferences(
            "theme.gitly.prefs",
            Context.MODE_PRIVATE
        )
    }

    // Theme
    private val _liveTheme: MutableLiveData<KThemes> = MutableLiveData<KThemes>()
    val liveTheme: LiveData<KThemes> get() = _liveTheme

    // Get the current theme
    val currentTheme: KThemes
        get() = KThemes.valueOf(
            prefs.getString("key.theme.prefs", KThemes.FOLLOW_SYSTEM.name)
                ?: KThemes.FOLLOW_SYSTEM.name
        )

    init {
        _liveTheme.value = KThemes.valueOf(
            prefs.getString("key.theme.prefs", KThemes.FOLLOW_SYSTEM.name)
                ?: KThemes.FOLLOW_SYSTEM.name
        )
    }

    // Update key
    fun updateTheme(themeKey: KThemes) {
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
        }

        prefs.edit {
            putString("key.theme.prefs", themeKey.name)
            apply()
        }
        _liveTheme.postValue(themeKey)
    }


}