package dev.gitly.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitly.debugger
import dev.gitly.model.sources.remote.WebService
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val webService: WebService) : ViewModel() {

    fun login() {
        debugger("Logging in...")
        viewModelScope.launch {

        }
    }

}