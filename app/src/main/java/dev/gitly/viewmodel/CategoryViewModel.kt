package dev.gitly.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitly.model.data.Category
import dev.gitly.model.repositories.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel @ViewModelInject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    val allCategories: LiveData<List<Category>> get() = repository.showAllCategories()

    fun getCategoryById(id: String): LiveData<Category> = repository.getCategoryById(id)

    fun updateCategory(category: Category) =
        viewModelScope.launch { repository.updateCategory(category.copy(isEnabled = !category.isEnabled)) }
}