package dev.gitly.core.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

/**
 * [SharedPreferences] implementation
 */
class AuthPrefs @Inject constructor(context: Context) {
    private val prefs by lazy { context.getSharedPreferences("gitly.prefs", Context.MODE_PRIVATE) }

    val token: String?
        get() = prefs.getString("token", null)

    fun logout() {
        prefs.edit {
            clear()
            apply()
        }
    }

    fun login(token: String?) {
        prefs.edit {
            putString("token", token)
            apply()
        }
    }
}