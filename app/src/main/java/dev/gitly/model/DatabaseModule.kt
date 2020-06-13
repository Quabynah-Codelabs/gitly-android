package dev.gitly.model

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.model.repositories.UserRepository
import dev.gitly.model.repositories.UserRepositoryImpl
import dev.gitly.model.sources.local.GitlyLocalDatabase
import dev.gitly.model.sources.local.UserDao
import dev.gitly.model.sources.local.UserLocalDataSource
import dev.gitly.model.sources.local.UserLocalDataSourceImpl
import dev.gitly.model.sources.remote.UserRemoteDataSource
import dev.gitly.model.sources.remote.UserRemoteDataSourceImpl
import dev.gitly.model.sources.remote.WebService
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): GitlyLocalDatabase =
        GitlyLocalDatabase.setup(context)

    @Singleton
    @Provides
    fun provideAuthPrefs(@ApplicationContext context: Context): AuthPrefs = AuthPrefs(context)

    @Provides
    fun provideUserDao(database: GitlyLocalDatabase): UserDao = database.userDao()

    @Provides
    fun provideUserLocalDataSource(userDao: UserDao, authPrefs: AuthPrefs): UserLocalDataSource =
        UserLocalDataSourceImpl(userDao, authPrefs)

    @Provides
    fun provideUserRemoteDataSource(
        webService: WebService,
        authPrefs: AuthPrefs
    ): UserRemoteDataSource =
        UserRemoteDataSourceImpl(webService, authPrefs)

    @Provides
    fun provideUserRepository(
        localDataSource: UserLocalDataSource,
        remoteDataSource: UserRemoteDataSource
    ): UserRepository = UserRepositoryImpl(localDataSource, remoteDataSource)
}