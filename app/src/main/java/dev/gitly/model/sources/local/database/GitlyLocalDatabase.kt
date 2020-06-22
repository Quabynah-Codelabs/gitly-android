package dev.gitly.model.sources.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.gitly.BuildConfig
import dev.gitly.debugger
import dev.gitly.model.data.User
import dev.gitly.model.sources.local.daos.UserDao
import javax.inject.Singleton

@Singleton
@Database(entities = [User::class], version = 1, exportSchema = true)
abstract class GitlyLocalDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: GitlyLocalDatabase? = null

        fun setup(context: Context): GitlyLocalDatabase = instance
            ?: synchronized(this) {
                instance
                    ?: if (BuildConfig.DEBUG)
                        Room.inMemoryDatabaseBuilder(context, GitlyLocalDatabase::class.java)
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    debugger("Gitly local database created")
                                }
                            })
                            .fallbackToDestructiveMigrationOnDowngrade()
                            .addMigrations(object : Migration(1, 2) {
                                override fun migrate(database: SupportSQLiteDatabase) {
                                    // do nothing
                                }
                            })
                            .build().also { instance = it }
                    else
                        Room.databaseBuilder(
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
                            .fallbackToDestructiveMigrationOnDowngrade()
                            .addMigrations(object : Migration(1, 2) {
                                override fun migrate(database: SupportSQLiteDatabase) {
                                    // do nothing
                                }
                            })
                            .build().also { instance = it }
            }
    }
}