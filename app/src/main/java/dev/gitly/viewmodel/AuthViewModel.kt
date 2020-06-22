package dev.gitly.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.debugger
import dev.gitly.model.repositories.UserRepository
import dev.gitly.model.sources.remote.service.LoginRequest
import dev.gitly.model.sources.remote.service.RegisterRequest
import dev.gitly.model.sources.remote.service.WebService
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
    private val webService: WebService,
    private val userRepository: UserRepository
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
                val accessToken = webService.login(
                    LoginRequest(
                        email,
                        password
                    )
                )

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

    // Register new user
    fun register(name: String, email: String, password: String) {
        if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {
            _authState.postValue(AuthState.ERROR)
            return
        }
        _authState.postValue(AuthState.AUTHENTICATING)
        viewModelScope.launch {
            try {
                // create new user
                val user = webService.signUp(
                    RegisterRequest(name, email, password)
                )

                // save user data
                userRepository.updateProfile(user, shouldSave = true)

                // get access token from server
                val accessToken = webService.login(
                    LoginRequest(
                        email,
                        password
                    )
                )

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