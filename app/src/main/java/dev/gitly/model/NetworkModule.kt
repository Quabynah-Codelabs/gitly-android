package dev.gitly.model

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.core.util.GitlyInterceptor
import dev.gitly.debugger
import dev.gitly.model.sources.remote.WebService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://10.0.2.2:5000/"

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class OAuthOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AuthorizedOkHttpClient

    @NetworkModule.AuthorizedOkHttpClient
    @Provides
    fun provideAuthorizedOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        gitlyInterceptor: GitlyInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(gitlyInterceptor)
            callTimeout(30L, TimeUnit.SECONDS)
            connectTimeout(60L, TimeUnit.SECONDS)
        }.build()

    @NetworkModule.OAuthOkHttpClient
    @Provides
    fun provideOAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            callTimeout(30L, TimeUnit.SECONDS)
            connectTimeout(60L, TimeUnit.SECONDS)
        }.build()

    @Provides
    fun provideGitlyInterceptor(authPrefs: AuthPrefs): GitlyInterceptor =
        GitlyInterceptor(authPrefs)

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                debugger(message)
            }
        }).apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    @Singleton
    @Provides
    fun provideWebService(
        prefs: AuthPrefs,
        @NetworkModule.AuthorizedOkHttpClient client: OkHttpClient
    ): WebService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WebService::class.java)
    }

}