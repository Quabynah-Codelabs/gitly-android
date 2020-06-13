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

    // Access token for user
    var token: String? = null
        get() = prefs.getString("token", null)
        set(value) {
            prefs.edit {
                putString("token", value)
                apply()
            }
            field = value
        }

    // User id
    var userId: String? = null
        get() = prefs.getString("id", null)
        set(value) {
            prefs.edit {
                putString("id", value)
                apply()
            }
            field = value
        }

    // Sign out current user
    fun logout() {
        // Alternatively
        //prefs.edit {
        //    clear()
        //    apply()
        //}
        this.token = null
        this.userId = null
    }

}