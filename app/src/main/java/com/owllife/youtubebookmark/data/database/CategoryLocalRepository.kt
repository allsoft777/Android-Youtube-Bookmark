package com.owllife.youtubebookmark.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Transaction
import com.owllife.youtubebookmark.domain.CategoryRepository
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.entity.CategoryEntity
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

    override fun observeCategories(): LiveData<List<CategoryEntity>> {
        return categoryDao.observeCategories()
    }

    override suspend fun queryCategories(): ResultData<List<CategoryEntity>> =
        withContext(ioDispatcher) {
            val result = categoryDao.queryCategories()
            return@withContext ResultData.Success(result)
        }

    override suspend fun insertNewCategory(item: CategoryEntity) = withContext(ioDispatcher) {
        categoryDao.insertNewCategory(item)
    }

    @Transaction
    override suspend fun deleteCategory(id: Int): Int = withContext(ioDispatcher) {
        categoryDao.deleteCategory(id)
        bookmarkDao.deleteBookmarkMatchingCategory(id)
    }

    override suspend fun updateCategory(category: CategoryEntity): Int =
        withContext(ioDispatcher) {
            categoryDao.updateCategory(category)
        }

    override suspend fun updateAll(categoryList: List<CategoryEntity>) = withContext(ioDispatcher) {
        categoryDao.updateAll(categoryList)
    }
}