package dev.gitly.model.sources.remote

import dev.gitly.model.data.AccessToken
import dev.gitly.model.data.User
import retrofit2.http.*

/**
 * Web service implementation
 */
interface WebService {

    @Headers("Accept: application/json")
    @POST("/oauth")
    suspend fun getAccessToken(
        @Body loginRequest: LoginRequest
    ): AccessToken

    @Headers("Accept: application/json")
    @GET("/api/users/me")
    suspend fun getCurrentUser(): User

    @Headers("Accept: application/json")
    @PUT("/api/users/me")
    suspend fun updateUserAsync(@Body user: User): User

    @DELETE("/api/users/{id}")
    suspend fun deleteUser(@Path("id") userId: String?)
}

/**
 * Login request body
 */
data class LoginRequest(
    val email: String?,
    val password: String
)