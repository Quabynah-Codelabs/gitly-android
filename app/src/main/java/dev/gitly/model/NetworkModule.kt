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
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://10.0.2.2:5000/"

    @Singleton
    @Provides
    fun provideWebService(prefs: AuthPrefs): WebService {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                debugger(message)
            }
        }).apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val gitlyInterceptor = GitlyInterceptor(prefs)
        val client = OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(gitlyInterceptor)
            callTimeout(30L, TimeUnit.SECONDS)
            connectTimeout(60L, TimeUnit.SECONDS)
        }.build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WebService::class.java)
    }

}