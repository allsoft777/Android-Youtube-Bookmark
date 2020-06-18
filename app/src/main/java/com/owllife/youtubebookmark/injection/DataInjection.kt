package com.owllife.youtubebookmark.injection

import android.content.Context
import com.owllife.youtubebookmark.data.database.BookmarkLocalRepository
import com.owllife.youtubebookmark.data.database.CategoryLocalRepository
import com.owllife.youtubebookmark.data.server.YoutubeRemoteRepositoryImpl
import com.owllife.youtubebookmark.domain.BookmarkRepository
import com.owllife.youtubebookmark.domain.CategoryRepository
import com.owllife.youtubebookmark.domain.YoutubeRemoteRepository

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
object DataInjection {

    fun provideCategoryLocalRepository(context: Context): CategoryRepository {
        return CategoryLocalRepository.getInstance(context)
    }

    fun provideYoutubeRemoteRepository(context: Context): YoutubeRemoteRepository {
        return YoutubeRemoteRepositoryImpl.getInstance(context)
    }

    fun provideBookmarkLocalRepository(context: Context): BookmarkRepository {
        return BookmarkLocalRepository.getInstance(context)
    }
}