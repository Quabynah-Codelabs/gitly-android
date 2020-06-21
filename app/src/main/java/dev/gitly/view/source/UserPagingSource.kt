package dev.gitly.view.source

import androidx.paging.PagingSource
import dev.gitly.model.data.User
import dev.gitly.model.repositories.UserRepository

/**
 * [PagingSource] for [User]s
 */
class UserPagingSource(private val repository: UserRepository, private val pageSize: Int) :
    PagingSource<Int, User>() {
    companion object {
        private const val GITLY_PAGING_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val position = params.key ?: GITLY_PAGING_INDEX
        val users = repository.getUsers(pageIndex = params.key ?: 0, pageSize = pageSize)
        return LoadResult.Page(
            data = users,
            nextKey = if (users.isEmpty()) null else position + 1,
            prevKey = if (position == GITLY_PAGING_INDEX) null else position - 1
        )
    }
}