package com.owllife.youtubebookmark.injection

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.owllife.youtubebookmark.data.firebase.ProfileRemoteRepository
import com.owllife.youtubebookmark.data.pref.SharedPreferenceRepository
import com.owllife.youtubebookmark.data.pref.SharedPreferenceWrapperImpl
import com.owllife.youtubebookmark.domain.interactor.FetchMyProfileUseCase
import com.owllife.youtubebookmark.domain.interactor.InsertProfileDataUseCase
import com.owllife.youtubebookmark.presentation.category.EditCategoryViewModel
import com.owllife.youtubebookmark.presentation.editbookmark.EditBookMarkViewModel
import com.owllife.youtubebookmark.presentation.login.LoginViewModel
import com.owllife.youtubebookmark.presentation.main.BookMarkListViewModel
import com.owllife.youtubebookmark.presentation.main.MainViewModel
import com.owllife.youtubebookmark.presentation.player.YoutubePlayerViewModel

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
                    DataInjection.provideCategoryLocalRepository(appContext)
                )
            isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(
                    appContext,
                    DataInjection.provideCategoryLocalRepository(appContext),
                    DataInjection.provideBookmarkLocalRepository(appContext)
                )
            isAssignableFrom(YoutubePlayerViewModel::class.java) ->
                YoutubePlayerViewModel(appContext)
            isAssignableFrom(EditBookMarkViewModel::class.java) ->
                EditBookMarkViewModel(
                    appContext,
                    DataInjection.provideCategoryLocalRepository(appContext),
                    DataInjection.provideBookmarkLocalRepository(appContext),
                    DataInjection.provideYoutubeRemoteRepository(appContext)
                )
            isAssignableFrom(BookMarkListViewModel::class.java) ->
                BookMarkListViewModel(
                    appContext,
                    DataInjection.provideBookmarkLocalRepository(appContext)
                )
            isAssignableFrom(LoginViewModel::class.java) -> {
                val profileRepo = ProfileRemoteRepository()
                LoginViewModel(
                    appContext,
                    SharedPreferenceRepository.getInstance(
                        appContext,
                        SharedPreferenceWrapperImpl()
                    ),
                    FetchMyProfileUseCase(profileRepo),
                    InsertProfileDataUseCase(profileRepo)
                )
            }
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}