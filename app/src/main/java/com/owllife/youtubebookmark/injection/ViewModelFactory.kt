package com.owllife.youtubebookmark.injection

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.owllife.youtubebookmark.presentation.category.EditCategoryViewModel
import com.owllife.youtubebookmark.presentation.editbookmark.EditBookMarkViewModel
import com.owllife.youtubebookmark.presentation.login.LoginViewModel
import com.owllife.youtubebookmark.presentation.main.BookMarkListViewModel
import com.owllife.youtubebookmark.presentation.main.MainViewModel
import com.owllife.youtubebookmark.presentation.player.YoutubePlayerViewModel
import com.owllife.youtubebookmark.presentation.profile.ProfileViewModel

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
class ViewModelFactory(private val appContext: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EditCategoryViewModel::class.java) ->
                EditCategoryViewModel(
                    appContext,
                    provideCategoryLocalRepository(appContext)
                )
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(
                    provideCategoryLocalRepository(appContext),
                    provideSharedPref(appContext)
                )
            modelClass.isAssignableFrom(YoutubePlayerViewModel::class.java) ->
                YoutubePlayerViewModel(
                    provideBookmarkLocalRepository(appContext)
                )
            modelClass.isAssignableFrom(EditBookMarkViewModel::class.java) ->
                EditBookMarkViewModel(
                    appContext,
                    provideCategoryLocalRepository(appContext),
                    provideBookmarkLocalRepository(appContext),
                    provideYoutubeRemoteRepository(appContext)
                )
            modelClass.isAssignableFrom(BookMarkListViewModel::class.java) ->
                BookMarkListViewModel(
                    appContext,
                    provideBookmarkLocalRepository(appContext)
                )
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(
                    appContext,
                    provideFetchMyProfileUseCase(appContext),
                    provideInsertProfileDataUseCase(),
                    provideSignInWithGoogleUseCase()
                )
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(
                    appContext,
                    provideFetchMyProfileUseCase(appContext),
                    provideSignOutUseCase(appContext)
                )
            }
            else -> throw IllegalArgumentException("invalid view model class.")
        } as T
    }
}