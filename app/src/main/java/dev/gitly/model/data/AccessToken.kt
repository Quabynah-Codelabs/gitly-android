package dev.gitly.model.data

import com.google.gson.annotations.SerializedName

/**
 * Access token after authentication
 */
data class AccessToken(
    @field:SerializedName("access_token") val token: String,
    @field:SerializedName("token_type") val type: String,
)