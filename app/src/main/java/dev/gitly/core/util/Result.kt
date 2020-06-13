package dev.gitly.core.util

/**
 * Result wrapper
 */
sealed class Result<T> {
    data class Success<T>(val data: T?) : Result<T>()
    data class Error<T>(val error: String) : Result<T>()
    class Loading<T> : Result<T>()
}

/**
 * Extension for success subclass
 */
val <T> Result<T>.isSuccessful get() = this is Result.Success<T>