package dev.gitly.model

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.core.util.AuthInterceptor
import dev.gitly.core.util.AuthInterceptorOkHttpClient
import dev.gitly.core.util.DefaultInterceptorOkHttpClient
import dev.gitly.debugger
import dev.gitly.model.sources.remote.WebService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityComponent::class)
object WebServiceUtil {
    private const val BASE_URL = "https://api.github.com/"
    private const val AUTH_BASE_URL = "https://github.com/"

    @Provides
    fun provideDefaultInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                debugger(message)
            }
        }).apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }

    @Provides
    fun provideAuthPrefs(context: Context): AuthPrefs = AuthPrefs(context)

    @Provides
    fun provideAuthInterceptor(prefs: AuthPrefs): AuthInterceptor = AuthInterceptor(prefs)

    @DefaultInterceptorOkHttpClient
    @Provides
    fun provideDefaultOkHttpClient(
        logging: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(logging)
    }.build()

    @AuthInterceptorOkHttpClient
    @Provides
    fun provideAuthOkHttpClient(
        logging: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(logging)
    }.build()

    @Provides
    fun provideDefaultWebService(
        @DefaultInterceptorOkHttpClient
        client: OkHttpClient
    ): WebService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WebService::class.java)

    @Provides
    fun provideAuthWebService(
        @AuthInterceptorOkHttpClient
        client: OkHttpClient
    ): WebService = Retrofit.Builder()
        .baseUrl(AUTH_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WebService::class.java)

}