package dev.gitly.model

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dev.gitly.model.sources.local.GitlyLocalDatabase

@Module
@InstallIn(ActivityComponent::class)
object DatabaseUtil {

    @Provides
    fun provideDatabase(@ActivityContext context: Context): GitlyLocalDatabase =
        GitlyLocalDatabase.setup(context)

}