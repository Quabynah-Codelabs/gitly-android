package dev.gitly.model

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.model.repositories.CategoryRepository
import dev.gitly.model.repositories.CategoryRepositoryImpl
import dev.gitly.model.repositories.UserRepository
import dev.gitly.model.repositories.UserRepositoryImpl
import dev.gitly.model.sources.local.UserLocalDataSource
import dev.gitly.model.sources.local.UserLocalDataSourceImpl
import dev.gitly.model.sources.local.daos.CategoryDao
import dev.gitly.model.sources.local.daos.UserDao
import dev.gitly.model.sources.local.database.GitlyLocalDatabase
import dev.gitly.model.sources.remote.UserRemoteDataSource
import dev.gitly.model.sources.remote.UserRemoteDataSourceImpl
import dev.gitly.model.sources.remote.service.WebService
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): GitlyLocalDatabase =
        GitlyLocalDatabase.setup(context)

    @Provides
    fun provideUserDao(database: GitlyLocalDatabase): UserDao = database.userDao()

    @Provides
    fun provideCategoryDao(database: GitlyLocalDatabase): CategoryDao = database.categoryDao()

    @Singleton
    @Provides
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository =
        CategoryRepositoryImpl(categoryDao)

    @Singleton
    @Provides
    fun provideUserLocalDataSource(userDao: UserDao, authPrefs: AuthPrefs): UserLocalDataSource =
        UserLocalDataSourceImpl(userDao, authPrefs)

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(
        webService: WebService,
        authPrefs: AuthPrefs
    ): UserRemoteDataSource =
        UserRemoteDataSourceImpl(webService, authPrefs)

    @Singleton
    @Provides
    fun provideUserRepository(
        localDataSource: UserLocalDataSource,
        remoteDataSource: UserRemoteDataSource
    ): UserRepository = UserRepositoryImpl(localDataSource, remoteDataSource)
}