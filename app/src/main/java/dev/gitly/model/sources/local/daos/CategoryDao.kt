package dev.gitly.model.sources.local.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import dev.gitly.model.data.Category

/**
 * Category DAO
 */
@Dao
interface CategoryDao : BaseDao<Category> {

    @Query("select * from categories order by name desc")
    fun showAllCategories(): LiveData<List<Category>>

    @Query("select * from categories where id is :id order by name desc")
    fun getCategoryById(id: String): LiveData<Category>

}