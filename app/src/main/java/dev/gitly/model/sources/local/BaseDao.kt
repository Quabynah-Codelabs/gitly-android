package dev.gitly.model.sources.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

/**
 * Base Data Access Object
 */
interface BaseDao<M> {
    @Insert
    fun insert(item: M)

    @Delete
    fun delete(item: M)

    @Update
    fun update(item: M)
}