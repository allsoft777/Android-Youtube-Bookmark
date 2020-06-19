package com.owllife.youtubebookmark.injection

import android.content.Context
import com.owllife.youtubebookmark.data.database.BookmarkLocalRepository
import com.owllife.youtubebookmark.data.database.CategoryLocalRepository
import com.owllife.youtubebookmark.data.firebase.ProfileRemoteRepository
import com.owllife.youtubebookmark.data.pref.SharedPreferenceRepository
import com.owllife.youtubebookmark.data.pref.SharedPreferenceWrapperImpl
import com.owllife.youtubebookmark.data.server.YoutubeRemoteRepositoryImpl
import com.owllife.youtubebookmark.domain.*

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
fun provideCategoryLocalRepository(appContext: Context): CategoryRepository {
    return CategoryLocalRepository.getInstance(appContext)
}

fun provideYoutubeRemoteRepository(appContext: Context): YoutubeRemoteRepository {
    return YoutubeRemoteRepositoryImpl.getInstance(appContext)
}

fun provideBookmarkLocalRepository(appContext: Context): BookmarkRepository {
    return BookmarkLocalRepository.getInstance(appContext)
}

fun provideSharedPref(appContext: Context): SharedPref {
    return SharedPreferenceRepository.getInstance(appContext, SharedPreferenceWrapperImpl())
}

fun provideRemoteProfileRepository(): ProfileRepository {
    return ProfileRemoteRepository()
}