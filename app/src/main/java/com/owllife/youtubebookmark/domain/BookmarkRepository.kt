package com.owllife.youtubebookmark.domain

import androidx.lifecycle.LiveData
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
interface BookmarkRepository {

    fun observeBookmarks(categoryId: Int): LiveData<List<BookMarkEntity>>

    suspend fun insertNewBookmark(item: BookMarkEntity)

    suspend fun deleteBookmark(id: Int)

    suspend fun updateBookmark(item: BookMarkEntity): Int
}