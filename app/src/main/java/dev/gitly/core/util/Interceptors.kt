package dev.gitly.core.util

import androidx.hilt.Assisted
import dev.gitly.core.prefs.AuthPrefs
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


/**
 * Application's WebService interceptor
 */
class GitlyInterceptor @Inject constructor(
    @Assisted private val authPrefs: AuthPrefs
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.apply {
            headers("x-auth-token: ${authPrefs.token}")
            headers("Accept: application/json")
        }
        return chain.proceed(request)
    }
}