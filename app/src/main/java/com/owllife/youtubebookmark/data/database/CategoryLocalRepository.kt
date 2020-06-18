package com.owllife.youtubebookmark.data.database

import android.content.Context
import androidx.lifecycle.LiveData
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
    private var dao: CategoryDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CategoryRepository {

    private val TAG = CategoryLocalRepository::class.java.simpleName

    companion object {

        @Volatile
        private var INSTANCE: CategoryRepository? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): CategoryRepository {
            if (INSTANCE == null) {
                INSTANCE =
                    CategoryLocalRepository(AppDatabase.getInstance(context).getCategoryDao())
            }
            return INSTANCE!!
        }
    }

    override fun observeCategories(): LiveData<List<CategoryEntity>> {
        return dao.observeCategories()
    }

    override suspend fun queryCategories(): ResultData<List<CategoryEntity>> =
        withContext(ioDispatcher) {
            val result = dao.queryCategories()
            return@withContext ResultData.Success(result)
        }

    override suspend fun insertNewCategory(item: CategoryEntity) = withContext(ioDispatcher) {
        dao.insertNewCategory(item)
    }

    override suspend fun deleteCategory(id: Int) = withContext(ioDispatcher) {
        dao.deleteCategory(id)
    }

    override suspend fun updateCategory(category: CategoryEntity): Int =
        withContext(ioDispatcher) {
            dao.updateCategory(category)
        }

    override suspend fun updateAll(categoryList: List<CategoryEntity>) = withContext(ioDispatcher) {
        dao.updateAll(categoryList)
    }
}