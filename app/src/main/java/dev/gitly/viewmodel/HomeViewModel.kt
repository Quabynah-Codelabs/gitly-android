package dev.gitly.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitly.core.util.Result
import dev.gitly.core.util.isSuccessful
import dev.gitly.debugger
import dev.gitly.model.data.User
import dev.gitly.model.repositories.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val repository: UserRepository
) : ViewModel() {

    // Live data for current user
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    // Get the current user
    fun viewCurrentUserProfile() {
        viewModelScope.launch {
            repository.viewProfile { cb ->
                if (cb == null) return@viewProfile
                when {
                    cb.isSuccessful -> {
                        _currentUser.value = (cb as Result.Success).data
                    }
                    cb is Result.Loading -> {
                        // Do nothing
                        debugger("Loading user data...")
                    }
                    else -> {
                        // Error
                        _currentUser.value = null
                    }
                }
            }
        }
    }

}