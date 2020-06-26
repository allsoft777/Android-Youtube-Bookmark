package com.owllife.youtubebookmark.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Transaction
import com.owllife.youtubebookmark.data.database.entity.CategoryEntity
import com.owllife.youtubebookmark.domain.CategoryRepository
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.entity.CategoryEntireVO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
class CategoryLocalRepository(
    private var categoryDao: CategoryDao,
    private var bookmarkDao: BookmarkDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CategoryRepository {

    companion object {

        @Volatile
        private var INSTANCE: CategoryRepository? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): CategoryRepository {
            if (INSTANCE == null) {
                INSTANCE =
                    CategoryLocalRepository(
                        AppDatabase.getInstance(context).getCategoryDao(),
                        AppDatabase.getInstance(context).getBookmarkDao()
                    )
            }
            return INSTANCE!!
        }
    }

    override fun observeCategories(): LiveData<List<CategoryEntireVO>> {
        return Transformations.map(categoryDao.observeCategories()) { items ->
            items.map { it.toEntireVO() }
        }
    }

    override suspend fun queryCategories(): ResultData<List<CategoryEntireVO>> =
        withContext(ioDispatcher) {
            val result = categoryDao.queryCategories().map { it.toEntireVO() }
            return@withContext ResultData.Success(result)
        }

    override suspend fun insertNewCategory(item: CategoryEntireVO) = withContext(ioDispatcher) {
        categoryDao.insertNewCategory(CategoryEntity.newEntityWith(item))
    }

    @Transaction
    override suspend fun deleteCategory(id: Int): Int = withContext(ioDispatcher) {
        categoryDao.deleteCategory(id)
        bookmarkDao.deleteBookmarkMatchingCategory(id)
    }

    override suspend fun updateCategory(category: CategoryEntireVO): Int =
        withContext(ioDispatcher) {
            val target = CategoryEntity(category.order, category.name)
            target.id = category.id
            categoryDao.updateCategory(target)
        }
}