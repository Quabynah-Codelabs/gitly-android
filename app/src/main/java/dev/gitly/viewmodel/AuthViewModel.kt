package dev.gitly.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitly.debugger
import dev.gitly.model.sources.remote.WebService
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val webService: WebService) : ViewModel() {

    fun getAccessToken(code: String) {
        debugger("Logging in...")
        viewModelScope.launch {
            val accessToken = webService.loginAsync(code).await()
            debugger(accessToken)
        }
    }

    fun getRepos() {
        debugger("Logging in...")
        viewModelScope.launch {
            val repos = webService.getReposAsync("fs-opensource").await()
            debugger(repos)
        }
    }

}