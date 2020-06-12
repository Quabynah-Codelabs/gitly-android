package dev.gitly.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.core.util.AuthWebService
import dev.gitly.core.util.GitlyInterceptor
import dev.gitly.debugger
import dev.gitly.model.DatabaseUtil
import dev.gitly.model.WebServiceUtil
import dev.gitly.model.sources.remote.WebService
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import javax.inject.Inject

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(context) as T
    }

}

class AuthViewModel @Inject constructor(context: Context) : ViewModel() {

    private val webService: WebService by lazy {
        WebServiceUtil.provideAuthWebService(
            OkHttpClient.Builder()
                .addInterceptor(WebServiceUtil.provideLoggingInterceptor())
                .addInterceptor(GitlyInterceptor(AuthPrefs(context)))
                .build()
        )
    }

    fun getAccessToken(code: String?, prefs: AuthPrefs) {
        debugger("Logging in...")
        viewModelScope.launch {
            val accessToken = webService.loginAsync(code)
            debugger(accessToken.token)
            prefs.login(accessToken.token)
            //getRepos()
        }
    }

    fun getRepos() {
        viewModelScope.launch {
            val repos = webService.getReposAsync()
            debugger(repos.toString())
        }
    }

}