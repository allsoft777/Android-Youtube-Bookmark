package com.owllife.youtubebookmark.injection

import android.content.Context
import com.owllife.youtubebookmark.domain.interactor.FetchMyProfileUseCase
import com.owllife.youtubebookmark.domain.interactor.InsertProfileDataUseCase
import com.owllife.youtubebookmark.domain.interactor.SignInWithGoogleUseCase
import com.owllife.youtubebookmark.domain.interactor.SignOutUseCase

/**
 * @author owllife.dev
 * @since 20. 6. 19
 */
fun provideFetchMyProfileUseCase(appContext: Context): FetchMyProfileUseCase {
    return FetchMyProfileUseCase(
        provideSharedPref(appContext), provideRemoteProfileRepository()
    )
}

fun provideInsertProfileDataUseCase(): InsertProfileDataUseCase {
    return InsertProfileDataUseCase(provideRemoteProfileRepository())
}

fun provideSignOutUseCase(appContext: Context): SignOutUseCase {
    return SignOutUseCase(provideSharedPref(appContext))
}

fun provideSignInWithGoogleUseCase(): SignInWithGoogleUseCase {
    return SignInWithGoogleUseCase()
}
