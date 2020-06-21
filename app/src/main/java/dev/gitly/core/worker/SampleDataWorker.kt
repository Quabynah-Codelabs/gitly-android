package dev.gitly.core.worker

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.gitly.debugger
import dev.gitly.model.data.User
import dev.gitly.model.sources.local.database.GitlyLocalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStreamReader

/**
 * Worker for loading sample data into the database
 */
class SampleDataWorker @WorkerInject constructor(
    @Assisted private val context: Context, @Assisted params: WorkerParameters,
    private val database: GitlyLocalDatabase
) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        return try {
            withContext(Dispatchers.IO) {
                with(Gson()) {
                    val fromJson = fromJson<List<User>>(
                        InputStreamReader(context.assets.open("sample_users.json")),
                        object : TypeToken<List<User>>() {}.type
                    )
                    database.userDao().insertAll(fromJson.toMutableList())
                }
            }
            Result.success()
        } catch (ex: IOException) {
            debugger("Adding sample users exited with error -> ${ex.localizedMessage}")
            Result.failure()
        }
    }
}