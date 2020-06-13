package dev.gitly.model.sources.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dev.gitly.model.data.User

@Dao
interface UserDao : BaseDao<User> {

    @Query("select * from users order by id desc")
    fun getAllUsers(): LiveData<List<User>>

    @Query("select * from users where id is :id order by id desc")
    fun getUser(id: String): LiveData<User>
}