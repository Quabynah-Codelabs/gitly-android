package dev.gitly.model.sources.remote.service

import dev.gitly.model.data.AccessToken
import dev.gitly.model.data.User
import retrofit2.http.*

/**
 * Web service implementation
 */
interface WebService {

    @Headers("Accept: application/json")
//    @POST("/gitly-mobile/us-central1/gitly/oauth")
    @POST("/gitly/oauth")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): AccessToken

    @Headers("Accept: application/json")
//    @POST("/gitly-mobile/us-central1/gitly/users")
    @POST("/gitly/users")
    suspend fun signUp(
        @Body registerRequest: RegisterRequest
    ): User

    @Headers("Accept: application/json")
//    @GET("/gitly-mobile/us-central1/gitly/users/me")
    @GET("/gitly/users/me")
    suspend fun getCurrentUser(): User?

    @Headers("Accept: application/json")
//    @GET("/gitly-mobile/us-central1/gitly/users/{id}")
    @GET("/gitly/users/{id}")
    suspend fun getUserById(@Path("id") id: String): User?

    @Headers("Accept: application/json")
//    @PUT("/gitly-mobile/us-central1/gitly/users/me")
    @PUT("/gitly/users/me")
    suspend fun updateUserAsync(@Body user: User): User

//    @DELETE("/gitly-mobile/us-central1/gitly/users/{id}")
    @DELETE("/gitly/users/{id}")
    suspend fun deleteUser(@Path("id") userId: String?)

//    @POST("/gitly-mobile/us-central1/gitly/users/mentors")
    @POST("/gitly/users/mentors")
    suspend fun requestMentors(
        @Body request: PagingRequest
    ): List<User>
}

/**
 * Login request body
 */
data class LoginRequest(
    val email: String?,
    val password: String
)

/**
 * Register request body
 */
data class RegisterRequest(
    val name: String?,
    val email: String?,
    val password: String
)

/**
 * Page size request
 */
data class PagingRequest(
    val page_index: Int,
    val page_size: Int
)