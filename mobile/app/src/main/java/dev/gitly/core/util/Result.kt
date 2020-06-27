package dev.gitly.core.util

/**
 * Result wrapper
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T?) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * Extension for success subclass
 */
val <T> Result<T>.isSuccessful get() = this is Result.Success<T> && data != null