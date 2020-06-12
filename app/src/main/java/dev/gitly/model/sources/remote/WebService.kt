package dev.gitly.model.sources.remote

import dev.gitly.BuildConfig
import dev.gitly.model.data.AccessToken
import dev.gitly.model.data.Repo
import kotlinx.coroutines.Deferred
import retrofit2.http.*

// gitly://callback
/**
 * Web service implementation
 */
interface WebService {

    @FormUrlEncoded
    @POST("login/oauth/access_token")
    fun loginAsync(
        @Field("code") code: String,
        @Field("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Field("client_secret") clientSecret: String = BuildConfig.CLIENT_SECRET,
    ): Deferred<AccessToken>

    @GET("/users/{username}/repos")
    fun getReposAsync(@Path("username") username: String): Deferred<List<Repo>>
}