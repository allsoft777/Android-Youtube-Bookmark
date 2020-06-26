package com.owllife.youtubebookmark.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.owllife.youtubebookmark.data.database.entity.BookMarkEntity
import com.owllife.youtubebookmark.domain.BookmarkRepository
import com.owllife.youtubebookmark.entity.BookMarkEntireVO
import com.owllife.youtubebookmark.entity.BookMarkSimpleVO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
class BookmarkLocalRepository(
    private var dao: BookmarkDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BookmarkRepository {

    companion object {

        @Volatile
        private var INSTANCE: BookmarkRepository? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): BookmarkRepository {
            if (INSTANCE == null) {
                INSTANCE =
                    BookmarkLocalRepository(AppDatabase.getInstance(context).getBookmarkDao())
            }
            return INSTANCE!!
        }
    }

    override fun observeBookmarks(categoryId: Int): LiveData<List<BookMarkEntity>> {
        return dao.observeBookmarks(categoryId)
    }

    override suspend fun fetchBookMarksSimpleType(categoryId: Int): List<BookMarkSimpleVO> =
        withContext(ioDispatcher) {
            dao.fetchBookmarks(categoryId).map { it.toSimpleVO() }
        }

    override suspend fun fetchBookMarkEntireType(categoryId: Int): BookMarkEntireVO =
        withContext(ioDispatcher) {
            dao.fetchBookmark(categoryId).toEntireVO()
        }

    override suspend fun insertNewBookmark(item: BookMarkEntireVO) = withContext(ioDispatcher) {
        dao.insertNewBookmark(BookMarkEntity.newEntityWith(item))
    }

    override suspend fun deleteBookmark(id: Int) = withContext(ioDispatcher) {
        dao.deleteBookmark(id)
    }

    override suspend fun updateBookmark(item: BookMarkEntity): Int = withContext(ioDispatcher) {
        dao.updateBookmark(item)
    }
}