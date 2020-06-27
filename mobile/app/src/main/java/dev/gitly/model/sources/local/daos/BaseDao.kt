package dev.gitly.model.sources.local.daos

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Base Data Access Object
 */
interface BaseDao<M> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: M)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(item: MutableList<M>)

    @Delete
    suspend fun delete(item: M)

    @Update
    suspend fun update(item: M)
}