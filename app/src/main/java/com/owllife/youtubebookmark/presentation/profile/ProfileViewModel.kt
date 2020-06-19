package com.owllife.youtubebookmark.presentation.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.exception.ExceptionData
import com.owllife.youtubebookmark.domain.firebase.MyProfileData
import com.owllife.youtubebookmark.domain.interactor.FetchMyProfileUseCase
import com.owllife.youtubebookmark.domain.interactor.SignOutUseCase
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 19
 */
class ProfileViewModel(
    appContext: Context,
    private val fetchMyProfileUseCase: FetchMyProfileUseCase,
    private val signOutUseCase: SignOutUseCase
) : BaseViewModel(appContext) {

    private var _profileData: MutableLiveData<MyProfileData> = MutableLiveData()
    var profileData: LiveData<MyProfileData> = _profileData

    fun loadProfile() {
        handleDataLoading(true)
        CoroutineScope(Dispatchers.Main).launch {
            handleDataLoading(false)
            val value = fetchMyProfileUseCase.execute(Unit)
            if (value is ResultData.Success) {
                _profileData.value = value.data
            } else if (value is ResultData.Failure) {
                if (value.exception is ExceptionData.NoErrException) {
                    _profileData.value = null
                } else {
                    handleFailure(value)
                }
            }
        }
    }

    fun logout() {
        CoroutineScope(Dispatchers.Main).launch {
            signOutUseCase.execute(Unit)
            setToastText(getString(R.string.msg_sign_out_successfully))
            finishScreen()
        }
    }
}