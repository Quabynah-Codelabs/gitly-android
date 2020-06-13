package dev.gitly.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.debugger
import dev.gitly.model.sources.remote.WebService
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    @Assisted private val authPrefs: AuthPrefs,
    @Assisted private val webService: WebService
) : ViewModel() {

    fun getAccessToken(code: String?) {
        viewModelScope.launch {
            val accessToken = webService.loginAsync(code)
            debugger(accessToken.token)
            authPrefs.login(accessToken.token)
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            val user = webService.getCurrentUserAsync()
            debugger(user)
        }
    }

}