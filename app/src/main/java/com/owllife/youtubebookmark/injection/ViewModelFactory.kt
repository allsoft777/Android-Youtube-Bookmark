package com.owllife.youtubebookmark.injection

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
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
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val appContext: Application,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(EditCategoryViewModel::class.java) ->
                EditCategoryViewModel(
                    appContext,
                    provideCategoryLocalRepository(appContext)
                )
            isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(
                    appContext,
                    provideCategoryLocalRepository(appContext)
                )
            isAssignableFrom(YoutubePlayerViewModel::class.java) ->
                YoutubePlayerViewModel(appContext)
            isAssignableFrom(EditBookMarkViewModel::class.java) ->
                EditBookMarkViewModel(
                    appContext,
                    provideCategoryLocalRepository(appContext),
                    provideBookmarkLocalRepository(appContext),
                    provideYoutubeRemoteRepository(appContext)
                )
            isAssignableFrom(BookMarkListViewModel::class.java) ->
                BookMarkListViewModel(
                    appContext,
                    provideBookmarkLocalRepository(appContext)
                )
            isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(
                    appContext,
                    provideFetchMyProfileUseCase(appContext),
                    provideInsertProfileDataUseCase(),
                    provideSignInWithGoogleUseCase()
                )
            }
            isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(
                    appContext,
                    provideFetchMyProfileUseCase(appContext),
                    provideSignOutUseCase(appContext)
                )
            }
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}