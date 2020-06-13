package dev.gitly.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.debugger
import dev.gitly.model.data.User
import dev.gitly.model.sources.remote.LoginRequest
import dev.gitly.model.sources.remote.WebService
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Authentication states
 */
enum class AuthState {
    AUTHENTICATING, AUTHENTICATED, ERROR, NONE
}

/**
 * Factory for [AuthViewModel]
 */
class AuthViewModelFactory @Inject constructor(
    @Assisted private val prefs: AuthPrefs,
    @Assisted private val service: WebService
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(prefs, service) as T
    }

}

/**
 * [ViewModel] for authentication
 */
class AuthViewModel @ViewModelInject constructor(
    @Assisted private val authPrefs: AuthPrefs,
    @Assisted private val webService: WebService
) : ViewModel() {

    // Live data for authentication state
    private val _authState = MutableLiveData(AuthState.NONE)
    val authState: LiveData<AuthState> get() = _authState

    // Live data for current user
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    // Get access token
    fun getAccessToken(email: String, password: String) {
        _authState.postValue(AuthState.AUTHENTICATING)
        viewModelScope.launch {
            try {
                val accessToken = webService.getAccessToken(LoginRequest(email, password))
                debugger(accessToken.token)
                authPrefs.token = accessToken.token
                val user = webService.getCurrentUser()
                authPrefs.userId = user.id
                _authState.postValue(AuthState.AUTHENTICATED)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
                _authState.postValue(AuthState.ERROR)
            }
        }
    }

    // Get the current user
    fun getCurrentUser() {
        viewModelScope.launch {
            try {
                val user = webService.getCurrentUser()
                _currentUser.postValue(user)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
                _currentUser.postValue(null)
            }
        }
    }

    // Sign out
    fun logout() {
        viewModelScope.launch {
            authPrefs.logout()
            _currentUser.value = null
        }
    }

}