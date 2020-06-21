package dev.gitly.model.sources.local

import androidx.lifecycle.asLiveData
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.model.data.User
import dev.gitly.model.sources.local.daos.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Local data source for [User]
 */
interface UserLocalDataSource {
    suspend fun getCurrentUser(): User?

    suspend fun getUserById(id: String): User?

    suspend fun updateUser(user: User)

    suspend fun deleteUser()

    suspend fun getUsers(pageIndex: Int, pageSize: Int): List<User>

    suspend fun saveAll(users: MutableList<User>)

    suspend fun save(user: User)
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

    override suspend fun getUserById(id: String): User? {
        return withContext(Dispatchers.IO) {
            dao.getUser(id)
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

    override suspend fun getUsers(pageIndex: Int, pageSize: Int): List<User> =
        dao.getUsersIndexed(pageIndex, pageSize)

    override suspend fun saveAll(users: MutableList<User>) {
        return withContext(Dispatchers.IO) {
            dao.insertAll(users)
        }
    }

    override suspend fun save(user: User) {
        return withContext(Dispatchers.IO) {
            dao.insert(user)
        }
    }

}