package dev.gitly.model.sources.local.daos

import androidx.room.Dao
import androidx.room.Query
import dev.gitly.model.data.User

@Dao
interface UserDao : BaseDao<User> {

    @Query("select * from users where id is :id order by id desc")
    suspend fun getUser(id: String?): User?

    @Query("delete from users where id is :id")
    suspend fun delete(id: String?)
}