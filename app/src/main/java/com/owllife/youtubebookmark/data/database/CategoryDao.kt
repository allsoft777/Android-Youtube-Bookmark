package com.owllife.youtubebookmark.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.owllife.youtubebookmark.domain.entity.CategoryEntity

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun observeCategories(): LiveData<List<CategoryEntity>>

    @Query("SELECT * FROM category")
    suspend fun queryCategories(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCategory(item: CategoryEntity)

    @Query("DELETE FROM category WHERE id LIKE :id")
    suspend fun deleteCategory(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCategory(category: CategoryEntity): Int

    @Update
    suspend fun updateAll(categoryList: List<CategoryEntity>)
}