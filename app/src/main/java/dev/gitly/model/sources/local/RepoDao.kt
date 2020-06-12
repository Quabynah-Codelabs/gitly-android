package dev.gitly.model.sources.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dev.gitly.model.data.Repo

@Dao
interface RepoDao : BaseDao<Repo> {

    @Query("select * from repos order by id desc")
    fun getAllRepos(): LiveData<List<Repo>>
}