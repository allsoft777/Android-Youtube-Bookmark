package com.owllife.youtubebookmark.presentation.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.exception.ExceptionData
import com.owllife.youtubebookmark.domain.firebase.MyProfileData
import com.owllife.youtubebookmark.domain.interactor.FetchMyProfileUseCase
import com.owllife.youtubebookmark.domain.interactor.SignOutUseCase
import com.owllife.youtubebookmark.presentation.data.FinishScreenData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 19
 */
class ProfileViewModel(
    private val appContext: Context,
    private val fetchMyProfileUseCase: FetchMyProfileUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private var _profileData: MutableLiveData<MyProfileData> = MutableLiveData()
    var profileData: LiveData<MyProfileData> = _profileData

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _toastText: MutableLiveData<String> = MutableLiveData()
    val toastText: LiveData<String> get() = _toastText

    private var _finishScreenData: MutableLiveData<FinishScreenData> = MutableLiveData()
    var finishScreenData: LiveData<FinishScreenData> = _finishScreenData

    private var _failureData: MutableLiveData<ResultData.Failure> = MutableLiveData()
    var failureData: LiveData<ResultData.Failure> = _failureData

    fun loadProfile() {
        _dataLoading.value = true
        CoroutineScope(Dispatchers.Main).launch {
            val value = fetchMyProfileUseCase.execute(Unit)
            _dataLoading.value = false
            if (value is ResultData.Success) {
                _profileData.value = value.data
            } else if (value is ResultData.Failure) {
                if (value.exception is ExceptionData.NoErrException) {
                    _profileData.value = null
                } else {
                    _failureData.value = value
                }
            }
        }
    }

    fun logout() {
        CoroutineScope(Dispatchers.Main).launch {
            signOutUseCase.execute(Unit)
            _toastText.value = appContext.getString(R.string.msg_sign_out_successfully)
            finishScreen(FinishScreenData.NoData)
        }
    }

    private fun finishScreen(data: FinishScreenData?) {
        _finishScreenData.value = data ?: FinishScreenData.NoData
    }
}