package dev.gitly.core.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.gitly.debugger
import javax.inject.Inject

/**
 * [SharedPreferences] implementation for authentication
 */
class AuthPrefs @Inject constructor(context: Context) {
    private val prefs by lazy { context.getSharedPreferences("gitly.prefs", Context.MODE_PRIVATE) }

    // live token listener
    private val _liveUserId = MutableLiveData<String?>()
    val refreshedUserId: LiveData<String?> get() = _liveUserId

    init {
        _liveUserId.postValue(prefs.getString("id", null))
    }

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
            _liveUserId.postValue(value)
            field = value
        }

    // Sign out current user
    fun logout() {
        debugger("Signing out...")
        this.token = null
        this.userId = null
    }

}