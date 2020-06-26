package com.owllife.youtubebookmark.domain

import androidx.lifecycle.LiveData
import androidx.room.Transaction
import com.owllife.youtubebookmark.entity.CategoryEntireVO

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
interface CategoryRepository {

    fun observeCategories(): LiveData<List<CategoryEntireVO>>

    suspend fun queryCategories(): ResultData<List<CategoryEntireVO>>

    suspend fun insertNewCategory(item: CategoryEntireVO)

    @Transaction
    suspend fun deleteCategory(id: Int): Int

    suspend fun updateCategory(category: CategoryEntireVO): Int
}