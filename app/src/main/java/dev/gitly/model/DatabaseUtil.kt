package dev.gitly.model

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.model.sources.local.GitlyLocalDatabase

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseUtil {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): GitlyLocalDatabase =
        GitlyLocalDatabase.setup(context)

    @Provides
    fun provideAuthPrefs(@ApplicationContext context: Context): AuthPrefs = AuthPrefs(context)
}