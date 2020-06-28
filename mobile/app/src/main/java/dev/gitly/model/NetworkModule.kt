package dev.gitly.model

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.gitly.BuildConfig
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.core.util.GitlyInterceptor
import dev.gitly.debugger
import dev.gitly.model.sources.remote.service.SlackWebService
import dev.gitly.model.sources.remote.service.WebService
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

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class OAuthOkHttpClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AuthorizedOkHttpClient

    //@Provides
    //fun provideContext(app: GitlyApp): Context = app.applicationContext

    @NetworkModule.AuthorizedOkHttpClient
    @Provides
    fun provideAuthorizedOkHttpClient(
        // context: Context,
        loggingInterceptor: HttpLoggingInterceptor,
        gitlyInterceptor: GitlyInterceptor
    ): OkHttpClient {
        // val cache = Cache(File(context.cacheDir, "http-cache"), 10 * 1024 * 1024)
        return OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            //  cache(cache)
            addInterceptor(gitlyInterceptor)
            callTimeout(30L, TimeUnit.SECONDS)
            connectTimeout(60L, TimeUnit.SECONDS)
        }.build()
    }

    @NetworkModule.OAuthOkHttpClient
    @Provides
    fun provideOAuthOkHttpClient(
        // context: Context,
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        // val cache = Cache(File(context.cacheDir, "http-cache"), 10 * 1024 * 1024)
        return OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            // cache(cache)
            callTimeout(30L, TimeUnit.SECONDS)
            connectTimeout(60L, TimeUnit.SECONDS)
        }.build()
    }

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
        @NetworkModule.AuthorizedOkHttpClient client: OkHttpClient
    ): WebService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WebService::class.java)
    }

    @Singleton
    @Provides
    fun provideSlackWebService(
        @NetworkModule.AuthorizedOkHttpClient client: OkHttpClient
    ): SlackWebService {
        return Retrofit.Builder()
            .baseUrl("https://api.slack.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SlackWebService::class.java)
    }

}