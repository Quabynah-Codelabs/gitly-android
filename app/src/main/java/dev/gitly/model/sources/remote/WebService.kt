package dev.gitly.model.sources.remote

import dev.gitly.model.data.Repo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Web service implementation
 */
interface WebService {

    @GET("/users/{username}/repos")
    fun getRepos(@Path("username") username: String): Call<List<Repo>>
}