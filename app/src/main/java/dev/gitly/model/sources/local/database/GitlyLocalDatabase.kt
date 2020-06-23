package dev.gitly.model.sources.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.gitly.BuildConfig
import dev.gitly.debugger
import dev.gitly.model.data.Category
import dev.gitly.model.data.User
import dev.gitly.model.sources.local.daos.CategoryDao
import dev.gitly.model.sources.local.daos.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import javax.inject.Singleton

@Singleton
@Database(entities = [User::class, Category::class], version = 2, exportSchema = true)
abstract class GitlyLocalDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao

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

                                    GlobalScope.launch(Dispatchers.IO) {
                                        with(Gson()) {
                                            val fromJson = fromJson<List<Category>>(
                                                InputStreamReader(context.assets.open("categories.json")),
                                                object : TypeToken<List<Category>>() {}.type
                                            )
                                            instance?.categoryDao()
                                                ?.insertAll(fromJson.toMutableList())
                                        }
                                    }
                                }
                            })
                            .fallbackToDestructiveMigrationOnDowngrade()
                            .addMigrations(object : Migration(1, 2) {
                                override fun migrate(database: SupportSQLiteDatabase) {
                                    // updated user class with `country` & `designation`
                                    // database.execSQL("")
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

                                    GlobalScope.launch(Dispatchers.IO) {
                                        with(Gson()) {
                                            val fromJson = fromJson<List<Category>>(
                                                InputStreamReader(context.assets.open("categories.json")),
                                                object : TypeToken<List<Category>>() {}.type
                                            )
                                            instance?.categoryDao()
                                                ?.insertAll(fromJson.toMutableList())
                                        }
                                    }

                                }
                            })
                            .fallbackToDestructiveMigrationOnDowngrade()
                            .addMigrations(object : Migration(1, 2) {
                                override fun migrate(database: SupportSQLiteDatabase) {
                                    // updated user class with `country` & `designation`
                                    // database.execSQL("")
                                }
                            })
                            .build().also { instance = it }
            }
    }
}