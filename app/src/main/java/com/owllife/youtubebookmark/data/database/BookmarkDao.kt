package com.owllife.youtubebookmark.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.owllife.youtubebookmark.data.database.entity.BookMarkEntity

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmark WHERE category_id LIKE :categoryId")
    fun observeBookmarks(categoryId: Int): LiveData<List<BookMarkEntity>>

    @Query("SELECT * FROM bookmark WHERE category_id LIKE :categoryId")
    suspend fun fetchBookmarks(categoryId: Int): List<BookMarkEntity>

    @Query("SELECT * FROM bookmark WHERE id LIKE :dbId")
    suspend fun fetchBookmark(dbId: Int): BookMarkEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewBookmark(item: BookMarkEntity)

    @Query("DELETE FROM bookmark WHERE id LIKE :id")
    suspend fun deleteBookmark(id: Int)

    @Query("DELETE FROM bookmark WHERE category_id LIKE :categoryId")
    suspend fun deleteBookmarkMatchingCategory(categoryId: Int): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBookmark(item: BookMarkEntity): Int
}