package dev.gitly.model.sources.remote

import androidx.hilt.Assisted
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.debugger
import dev.gitly.model.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Local data source for [User]
 */
interface UserRemoteDataSource {
    suspend fun getCurrentUser(): User?

    suspend fun updateUser(user: User)

    suspend fun deleteUser()

}

class UserRemoteDataSourceImpl @Inject constructor(
    @Assisted private val service: WebService,
    @Assisted private val prefs: AuthPrefs
) : UserRemoteDataSource {

    override suspend fun getCurrentUser(): User? {
        return try {
            service.getCurrentUser()
        } catch (e: Exception) {
            debugger(e.localizedMessage)
            null
        }
    }

    override suspend fun updateUser(user: User) {
        return withContext(Dispatchers.IO) {
            try {
                service.updateUserAsync(user)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
            }
        }
    }

    override suspend fun deleteUser() {
        return withContext(Dispatchers.IO) {
            try {
                service.deleteUser(prefs.userId)
            } catch (e: Exception) {
                debugger(e.localizedMessage)
            }
        }
    }

}