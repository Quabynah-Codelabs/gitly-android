package dev.gitly.model

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.core.util.AuthWebService
import dev.gitly.core.util.DefaultWebService
import dev.gitly.core.util.GitlyInterceptor
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
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                debugger(message)
            }
        }).apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    @Provides
    fun provideDefaultInterceptor(prefs: AuthPrefs): GitlyInterceptor = GitlyInterceptor(prefs)

    @Provides
    fun provideAuthOkHttpClient(
        interceptor: GitlyInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(loggingInterceptor)
        addInterceptor(interceptor)
    }.build()

    @DefaultWebService
    @Provides
    fun provideDefaultWebService(
        client: OkHttpClient
    ): WebService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WebService::class.java)

    @AuthWebService
    @Provides
    fun provideAuthWebService(
        client: OkHttpClient
    ): WebService = Retrofit.Builder()
        .baseUrl(AUTH_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WebService::class.java)

}