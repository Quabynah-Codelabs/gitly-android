package dev.gitly.model.sources.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Base Data Access Object
 */
interface BaseDao<M> {
    @Insert
    suspend fun insert(item: M)

    @Delete
    suspend fun delete(item: M)

    @Update
    suspend fun update(item: M)
}