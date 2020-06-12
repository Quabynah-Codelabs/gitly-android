package dev.gitly.core.util

import dev.gitly.core.prefs.AuthPrefs
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultInterceptorOkHttpClient

/**
 * Authentication interceptor
 */
class AuthInterceptor @Inject constructor(private val authPrefs: AuthPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.apply {
            header("Accept: application/json")
        }
        return chain.proceed(request)
    }

}