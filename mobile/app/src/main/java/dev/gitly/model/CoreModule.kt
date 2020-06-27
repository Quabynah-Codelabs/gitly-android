package dev.gitly.model

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.core.prefs.ThemePrefs
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CoreModule {

    @Singleton
    @Provides
    fun provideAuthPrefs(@ApplicationContext context: Context): AuthPrefs = AuthPrefs(context)

    @Singleton
    @Provides
    fun provideThemePrefs(@ApplicationContext context: Context): ThemePrefs = ThemePrefs(context)
}