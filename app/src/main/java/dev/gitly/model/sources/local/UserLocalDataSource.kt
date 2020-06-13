package dev.gitly.model.sources.local

import androidx.hilt.Assisted
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.model.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Local data source for [User]
 */
interface UserLocalDataSource {
    suspend fun getCurrentUser(): User?

    suspend fun updateUser(user: User)

    suspend fun deleteUser()
}

class UserLocalDataSourceImpl @Inject constructor(
    @Assisted private val dao: UserDao,
    @Assisted private val prefs: AuthPrefs
) : UserLocalDataSource {

    override suspend fun getCurrentUser(): User? {
        return withContext(Dispatchers.IO) {
            dao.getUser(prefs.userId)
        }
    }

    override suspend fun updateUser(user: User) {
        return withContext(Dispatchers.IO) {
            dao.update(user)
        }
    }

    override suspend fun deleteUser() {
        return withContext(Dispatchers.IO) {
            dao.delete(prefs.userId)
        }
    }

}