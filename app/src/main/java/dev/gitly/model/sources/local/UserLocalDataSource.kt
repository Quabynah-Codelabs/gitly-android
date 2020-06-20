package dev.gitly.model.sources.local

import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.model.data.User
import dev.gitly.model.sources.local.daos.UserDao
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
    private val dao: UserDao,
    private val prefs: AuthPrefs
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