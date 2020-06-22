package dev.gitly.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitly.model.data.User
import kotlinx.coroutines.launch

enum class SearchState {
    NONE, STARTED, COMPLETED, ERROR
}

class SearchViewModel @ViewModelInject constructor() : ViewModel() {

    // Live data
    private val _searchResult = MutableLiveData<List<User>>()
    val searchResult: LiveData<List<User>> get() = _searchResult
    private val _searchState = MutableLiveData(SearchState.NONE)
    val searchState: LiveData<SearchState> get() = _searchState


    fun searchFor(query: String) {
        _searchState.value = SearchState.STARTED
        viewModelScope.launch {
            // TODO: Perform search
        }
    }

}