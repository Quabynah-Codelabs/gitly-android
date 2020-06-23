package dev.gitly.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.gitly.model.data.User
import dev.gitly.model.repositories.UserRepository
import dev.gitly.view.source.UserPagingSource
import kotlinx.coroutines.flow.Flow

class UserViewModel @ViewModelInject constructor(
    private val repository: UserRepository
) : ViewModel() {

    // Live data for current user
    val currentUser: LiveData<User> get() = repository.getCurrentUser()

    private var currentUsersResult: Flow<PagingData<User>>? = null

    // Get a stream of users
    fun getUsersStream(): Flow<PagingData<User>> {
        return currentUsersResult ?: Pager(
            config = PagingConfig(pageSize = USER_PAGE_SIZE),
            pagingSourceFactory = {
                UserPagingSource(
                    repository,
                    USER_PAGE_SIZE
                )
            }
        ).flow.cachedIn(viewModelScope).also { currentUsersResult = it }
    }

    companion object {
        private const val USER_PAGE_SIZE = 5
    }
}