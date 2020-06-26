package com.owllife.youtubebookmark.domain

import androidx.lifecycle.LiveData
import com.owllife.youtubebookmark.data.database.entity.BookMarkEntity
import com.owllife.youtubebookmark.entity.BookMarkEntireVO
import com.owllife.youtubebookmark.entity.BookMarkSimpleVO

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
interface BookmarkRepository {

    fun observeBookmarks(categoryId: Int): LiveData<List<BookMarkEntity>>

    suspend fun fetchBookMarksSimpleType(categoryId: Int): List<BookMarkSimpleVO>

    suspend fun fetchBookMarkEntireType(categoryId: Int): BookMarkEntireVO

    suspend fun insertNewBookmark(item: BookMarkEntireVO)

    suspend fun deleteBookmark(id: Int)

    suspend fun updateBookmark(item: BookMarkEntity): Int
}