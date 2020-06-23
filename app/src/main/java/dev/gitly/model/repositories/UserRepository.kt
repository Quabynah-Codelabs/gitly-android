package dev.gitly.model.repositories

import androidx.hilt.Assisted
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dev.gitly.model.data.User
import dev.gitly.model.sources.local.UserLocalDataSource
import dev.gitly.model.sources.remote.UserRemoteDataSource
import javax.inject.Inject

interface UserRepository {

    fun getCurrentUser(): LiveData<User>

    suspend fun updateProfile(user: User, shouldSave: Boolean = false)

    suspend fun deleteAccount(isAdmin: Boolean)

    suspend fun getUsers(pageIndex: Int, pageSize: Int): List<User>

    fun getUserById(id: String): LiveData<User>
}

/**
 * Implementation of [UserRepository]
 */
class UserRepositoryImpl @Inject constructor(
    @Assisted private val local: UserLocalDataSource,
    @Assisted private val remote: UserRemoteDataSource
) : UserRepository {

    override fun getCurrentUser(): LiveData<User> = liveData {
        emitSource(local.getCurrentUser())

        // fetch from server
        val remoteUser = remote.getCurrentUser()

        if (remoteUser != null) {
            // update cache
            local.save(remoteUser)
        }
    }

    override suspend fun updateProfile(user: User, shouldSave: Boolean) {
        if (shouldSave) {
            local.save(user)
        } else {
            local.updateUser(user)
            remote.updateUser(user)
        }
    }

    override suspend fun deleteAccount(isAdmin: Boolean) {
        if (!isAdmin) return
        local.deleteUser()
        remote.deleteUser()
    }

    override suspend fun getUsers(pageIndex: Int, pageSize: Int): List<User> {
        val users = remote.getUsers(pageIndex, pageSize)
        if (users.isNotEmpty()) local.saveAll(users.toMutableList())
        return local.getUsers(pageIndex, pageSize)
    }

    override fun getUserById(id: String): LiveData<User> = liveData {
        emitSource(local.getUserById(id))
        val user = remote.getUserById(id)
        if (user != null) local.save(user)
    }

}