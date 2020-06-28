package dev.gitly.view.source

import androidx.paging.PagingSource
import dev.gitly.debugger
import dev.gitly.model.data.User
import dev.gitly.model.repositories.UserRepository
import retrofit2.HttpException
import java.io.IOException

/**
 * [PagingSource] for [User]s
 */
class UserPagingSource(private val repository: UserRepository, private val pageSize: Int) :
    PagingSource<Int, User>() {
    companion object {
        private const val GITLY_PAGING_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val position = params.key ?: GITLY_PAGING_INDEX
            val users = repository.getUsers(pageIndex = position, pageSize = pageSize)
            LoadResult.Page(
                data = users,
                prevKey = null,
                nextKey = if (position == GITLY_PAGING_INDEX) null else position - 1
            )
        } catch (e: IOException) {
            // IOException for network failures.
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            LoadResult.Error(e)
        }
    }
}