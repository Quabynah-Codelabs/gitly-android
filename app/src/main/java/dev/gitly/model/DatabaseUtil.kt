package dev.gitly.model

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.model.sources.local.GitlyLocalDatabase
import dev.gitly.model.sources.local.UserDao
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
object DatabaseUtil {

    @Singleton
    @Provides
    fun provideDatabase(/*@ActivityContext */context: Context): GitlyLocalDatabase =
        GitlyLocalDatabase.setup(context)

    @Singleton
    @Provides
    fun provideAuthPrefs(/*@ActivityContext*/ context: Context): AuthPrefs = AuthPrefs(context)

    @Provides
    fun provideUserDao(database: GitlyLocalDatabase): UserDao = database.userDao()
}