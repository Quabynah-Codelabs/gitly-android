package dev.gitly.core.worker

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.gitly.debugger
import dev.gitly.model.sources.local.database.GitlyLocalDatabase
import java.io.IOException

/**
 * Worker for loading sample data into the database
 */
class SampleDataWorker @WorkerInject constructor(
    @Assisted private val context: Context, @Assisted params: WorkerParameters,
    private val database: GitlyLocalDatabase
) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        // todo: load some sample data from server
        return try {
            /*withContext(Dispatchers.IO) {
                with(Gson()) {
                    val fromJson = fromJson<List<User>>(
                        InputStreamReader(context.assets.open("sampledata/users.json")),
                        object : TypeToken<List<User>>() {}.type
                    )
                    database.userDao().insertAll(fromJson.toMutableList())
                }
            }*/
            Result.success()
        } catch (ex: IOException) {
            debugger("Adding sample users exited with error -> ${ex.localizedMessage}")
            Result.failure()
        }
    }
}