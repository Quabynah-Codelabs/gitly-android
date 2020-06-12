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

    val userId: String?
        get() = prefs.getString("user_id", null)

    fun logout() {
        prefs.edit {
            clear()
            apply()
        }
    }

    fun login(userId: String, token: String?) {
        prefs.edit {
            putString("user_id", userId)
            putString("token", token)
            apply()
        }
    }
}