package dev.gitly.model.sources.remote

import dev.gitly.model.data.Repo
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

// gitly://callback
/**
 * Web service implementation
 */
interface WebService {

    @GET("/users/{username}/repos")
    fun getReposAsync(@Path("username") username: String): Deferred<List<Repo>>
}