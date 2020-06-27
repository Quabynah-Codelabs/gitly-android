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
    private val token = authPrefs.token

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) addHeader("x-auth-token", token)
        }.build()
        return chain.proceed(request)
    }
}