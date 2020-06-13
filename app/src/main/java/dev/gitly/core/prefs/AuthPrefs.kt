package dev.gitly.core.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [SharedPreferences] implementation
 */
@Singleton
class AuthPrefs @Inject constructor(context: Context) {
    private val prefs by lazy { context.getSharedPreferences("gitly.prefs", Context.MODE_PRIVATE) }

    val token: String?
        get() = prefs.getString("token", null)
    val userId: String?
        get() = prefs.getString("id", null)

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

    fun updateUserId(id: String?) {
        prefs.edit {
            putString("id", id)
            apply()
        }
    }
}