package dev.gitly.model.sources.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dev.gitly.model.data.User

@Dao
interface UserDao : BaseDao<User> {

    // order by id desc
    @Query("select * from users limit :pageSize offset :pageIndex")
    suspend fun getUsersIndexed(pageIndex: Int = 0, pageSize: Int = 10): List<User>

    @Query("select * from users limit :pageSize offset :pageIndex")
    fun getLiveUsersIndexed(pageIndex: Int = 0, pageSize: Int = 10): LiveData<List<User>>

    @Query("select * from users where id is :id order by id desc")
    suspend fun getUser(id: String?): User?

    @Query("delete from users where id is :id")
    suspend fun delete(id: String?)
}