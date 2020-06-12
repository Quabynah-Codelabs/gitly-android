package dev.gitly.core.util

import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.debugger
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthWebService

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultWebService

/**
 * Application's WebService interceptor
 */
class GitlyInterceptor @Inject constructor(private val authPrefs: AuthPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.apply {
            headers("Authorization: token ${authPrefs.token}")
            headers("Accept: application/json")
        }
        return chain.proceed(request)
    }
}