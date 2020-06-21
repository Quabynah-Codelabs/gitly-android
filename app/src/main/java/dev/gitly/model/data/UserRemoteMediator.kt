package dev.gitly.model.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.gitly.model.repositories.UserRepository

@ExperimentalPagingApi
class UserRemoteMediator(
    private val userRepository: UserRepository
) : RemoteMediator<Int, User>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        try {
            when (loadType) {
                LoadType.PREPEND -> {
                    val user = getUsersKeyForFirstItem(state)
                    return MediatorResult.Success(endOfPaginationReached = user != null)
                }

                LoadType.APPEND -> {
                    val user = getUsersKeyForLastItem(state)
                    return MediatorResult.Success(endOfPaginationReached = user != null)
                }

                LoadType.REFRESH -> {
                    val user = getUsersKeyForRefreshedItem(state)
                    return MediatorResult.Success(endOfPaginationReached = user != null)
                }
            }

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private fun getUsersKeyForFirstItem(state: PagingState<Int, User>): User? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()

    private fun getUsersKeyForLastItem(state: PagingState<Int, User>): User? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()

    private suspend fun getUsersKeyForRefreshedItem(state: PagingState<Int, User>): User? =
        state.anchorPosition.let { position ->
            state.closestItemToPosition(position ?: 0)?.id?.let {
                userRepository.getUserById(it)
            }
        }
}