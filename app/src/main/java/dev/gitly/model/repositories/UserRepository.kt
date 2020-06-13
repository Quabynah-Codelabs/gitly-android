package dev.gitly.model.repositories

import androidx.hilt.Assisted
import dev.gitly.core.util.Callback
import dev.gitly.core.util.Result
import dev.gitly.model.data.User
import dev.gitly.model.sources.local.UserLocalDataSource
import dev.gitly.model.sources.remote.UserRemoteDataSource
import javax.inject.Inject

interface UserRepository {
    suspend fun viewProfile(callback: Callback<Result<User>>)

    suspend fun updateProfile(user: User)

    suspend fun deleteAccount(isAdmin: Boolean)
}

/**
 * Implementation of [UserRepository]
 */
class UserRepositoryImpl @Inject constructor(
    @Assisted private val local: UserLocalDataSource,
    @Assisted private val remote: UserRemoteDataSource
) : UserRepository {
    override suspend fun viewProfile(callback: Callback<Result<User>>) {
        callback(Result.Loading())

        // fetch from cache
        val localUser = local.getCurrentUser()
        if (localUser != null) callback(Result.Success(localUser))

        // fetch from server
        val remoteUser = remote.getCurrentUser()
        if (remoteUser != null) {
            // update cache
            local.updateUser(remoteUser)
            callback(Result.Success(remoteUser))
        } else
            callback(Result.Error("No user found"))
    }

    override suspend fun updateProfile(user: User) {
        local.updateUser(user)
        remote.updateUser(user)
    }

    override suspend fun deleteAccount(isAdmin: Boolean) {
        if (!isAdmin) return
        local.deleteUser()
        remote.deleteUser()
    }

}