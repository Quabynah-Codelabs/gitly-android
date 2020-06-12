package dev.gitly.model.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.gitly.debugger
import dev.gitly.model.data.Repo

@Database(entities = [Repo::class], version = 1, exportSchema = true)
abstract class GitlyLocalDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao

    companion object {
        @Volatile
        private var instance: GitlyLocalDatabase? = null

        fun setup(context: Context): GitlyLocalDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context,
                GitlyLocalDatabase::class.java,
                "gitly_app_db"
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        debugger("Gitly local database created")
                    }
                })
                .build().also { instance = it }
        }
    }
}