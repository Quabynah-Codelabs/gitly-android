package dev.gitly.model.repositories

import androidx.lifecycle.LiveData
import dev.gitly.model.data.Category
import dev.gitly.model.sources.local.daos.CategoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface CategoryRepository {
    fun showAllCategories(): LiveData<List<Category>>

    fun getCategoryById(id: String): LiveData<Category>

    suspend fun updateCategory(category: Category)
}

class CategoryRepositoryImpl(private val dao: CategoryDao) : CategoryRepository {
    override fun showAllCategories(): LiveData<List<Category>> = dao.showAllCategories()

    override fun getCategoryById(id: String): LiveData<Category> = dao.getCategoryById(id)

    override suspend fun updateCategory(category: Category) = withContext(Dispatchers.IO) {
        dao.update(category)
    }
}