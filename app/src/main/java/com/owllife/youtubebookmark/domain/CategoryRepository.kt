package com.owllife.youtubebookmark.domain

import androidx.lifecycle.LiveData
import com.owllife.youtubebookmark.domain.entity.CategoryEntity

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
interface CategoryRepository {

    fun observeCategories(): LiveData<List<CategoryEntity>>

    suspend fun queryCategories(): ResultData<List<CategoryEntity>>

    suspend fun insertNewCategory(item: CategoryEntity)

    suspend fun deleteCategory(id: Int)

    suspend fun updateCategory(category: CategoryEntity): Int

    suspend fun updateAll(categoryList: List<CategoryEntity>)
}