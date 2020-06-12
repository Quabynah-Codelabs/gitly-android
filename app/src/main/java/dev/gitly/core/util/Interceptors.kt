package dev.gitly.core.util

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultInterceptorOkHttpClient


class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        request.apply {
            // TODO: Add headers here
        }
        return chain.proceed(request)
    }

}