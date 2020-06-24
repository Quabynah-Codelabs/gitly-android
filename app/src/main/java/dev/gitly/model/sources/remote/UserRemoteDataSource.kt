package dev.gitly.model.sources.remote

import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.debugger
import dev.gitly.model.data.User
import dev.gitly.model.sources.remote.service.PagingRequest
import dev.gitly.model.sources.remote.service.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Local data source for [User]
 */
interface UserRemoteDataSource {
    suspend fun getCurrentUser(): User?

    suspend fun getUserById(id: String): User?

    suspend fun updateUser(user: User)

    suspend fun deleteUser()

    suspend fun getUsers(pageIndex: Int, pageSize: Int): List<User>

}

class UserRemoteDataSourceImpl @Inject constructor(
    private val service: WebService,
    private val prefs: AuthPrefs
) : UserRemoteDataSource {

    override suspend fun getCurrentUser(): User? {
        return try {
            service.getCurrentUser()
        } catch (e: Exception) {
            debugger(e.localizedMessage)
            null
        }
    }

    override suspend fun getUserById(id: String): User? =
        withContext(Dispatchers.IO) {
            try {
                service.getUserById(id)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
                null
            }
        }

    override suspend fun updateUser(user: User) {
        withContext(Dispatchers.IO) {
            try {
                service.updateUserAsync(user)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
            }
        }
    }

    override suspend fun deleteUser() =
        withContext(Dispatchers.IO) {
            try {
                service.deleteUser(prefs.userId)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
            }
        }

    override suspend fun getUsers(pageIndex: Int, pageSize: Int): List<User> =
        withContext(Dispatchers.IO) {
            try {
                service.requestMentors(PagingRequest(pageIndex, pageSize))
            } catch (e: Exception) {
                listOf<User>()
            }
        }

}