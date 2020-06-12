package dev.gitly.model.sources.remote

import dev.gitly.BuildConfig
import dev.gitly.model.data.AccessToken
import dev.gitly.model.data.Repo
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.*

// gitly://callback
/**
 * Web service implementation
 */
// d1aef04b436b8f2df909ccbddf81884ed1289690
interface WebService {

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    suspend fun loginAsync(
        @Field("code") code: String?,
        @Field("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Field("client_secret") clientSecret: String = BuildConfig.CLIENT_SECRET,
    ): AccessToken

    @Headers("Accept: application/json")
    @GET("/user/repos")
    suspend fun getReposAsync(): ResponseBody
}