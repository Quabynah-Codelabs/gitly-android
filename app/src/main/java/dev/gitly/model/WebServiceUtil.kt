package dev.gitly.model

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dev.gitly.core.util.AuthInterceptor
import dev.gitly.core.util.AuthInterceptorOkHttpClient
import dev.gitly.core.util.DefaultInterceptorOkHttpClient
import dev.gitly.debugger
import dev.gitly.model.sources.remote.WebService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(ActivityComponent::class)
object WebServiceUtil {
    private const val BASE_URL = "https://api.github.com/"

    @Provides
    fun provideAuthInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                debugger(message)
            }
        }).apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
            // redactHeader("Authorization")
        }
    }

    @AuthInterceptorOkHttpClient
    @Provides
    fun provideAuthOkHttpClient(
        logging: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(logging)
        }.build()
    }

    @DefaultInterceptorOkHttpClient
    @Provides
    fun provideDefaultOkHttpClient(
        logging: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(logging)
        }.build()
    }

    @Provides
    fun provideWebService(
        @AuthInterceptorOkHttpClient
        client: OkHttpClient
    ): WebService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(WebService::class.java)
    }

}