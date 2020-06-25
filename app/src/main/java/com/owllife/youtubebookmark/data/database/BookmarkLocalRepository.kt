package com.owllife.youtubebookmark.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import com.owllife.youtubebookmark.domain.BookmarkRepository
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
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

    override suspend fun fetchBookmarks(categoryId: Int): List<BookMarkEntity> =
        withContext(ioDispatcher) {
            dao.fetchBookmarks(categoryId)
        }

    override suspend fun insertNewBookmark(item: BookMarkEntity) = withContext(ioDispatcher) {
        dao.insertNewBookmark(item)
    }

    override suspend fun deleteBookmark(id: Int) = withContext(ioDispatcher) {
        dao.deleteBookmark(id)
    }

    override suspend fun updateBookmark(item: BookMarkEntity): Int = withContext(ioDispatcher) {
        dao.updateBookmark(item)
    }
}