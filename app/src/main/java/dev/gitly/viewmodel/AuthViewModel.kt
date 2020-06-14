package dev.gitly.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.debugger
import dev.gitly.model.sources.remote.LoginRequest
import dev.gitly.model.sources.remote.WebService
import kotlinx.coroutines.launch

/**
 * Authentication states
 */
enum class AuthState {
    AUTHENTICATING, AUTHENTICATED, ERROR, NONE
}

/**
 * [ViewModel] for authentication
 */
class AuthViewModel @ViewModelInject constructor(
    private val authPrefs: AuthPrefs,
    private val webService: WebService
) : ViewModel() {

    // Live data for authentication state
    private val _authState = MutableLiveData(AuthState.NONE)
    val authState: LiveData<AuthState> get() = _authState

    // Get access token
    fun getAccessToken(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.postValue(AuthState.ERROR)
            return
        }
        _authState.postValue(AuthState.AUTHENTICATING)
        viewModelScope.launch {
            try {
                // get access token from server
                val accessToken = webService.getAccessToken(LoginRequest(email, password))
                // save token
                authPrefs.token = accessToken.token
                // update UI
                _authState.postValue(AuthState.AUTHENTICATED)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
                _authState.postValue(AuthState.ERROR)
            }
        }
    }

    // Sign out
    fun logout() {
        viewModelScope.launch {
            authPrefs.logout()
            _authState.value = AuthState.NONE
        }
    }

}