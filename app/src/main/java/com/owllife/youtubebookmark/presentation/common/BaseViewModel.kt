package com.owllife.youtubebookmark.presentation.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.owllife.youtubebookmark.domain.ResultData.Failure
import com.owllife.youtubebookmark.presentation.data.FinishScreenData

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
open class BaseViewModel(private val appContext: Context) : ViewModel() {

    private val _toastText: MutableLiveData<String> = MutableLiveData()
    val toastText: LiveData<String> = _toastText

    private var _failureData: MutableLiveData<Failure> = MutableLiveData()
    var failureData: LiveData<Failure> = _failureData

    private var _finishScreenData: MutableLiveData<FinishScreenData> = MutableLiveData()
    var finishScreenData: LiveData<FinishScreenData> = _finishScreenData

    protected fun setToastText(text: String) {
        _toastText.value = text
    }

    protected fun handleFailure(failure: Failure) {
        _failureData.value = failure
    }

    fun finishScreen() {
        finishScreen(FinishScreenData.NoData)
    }

    fun finishScreen(data: FinishScreenData?) {
        _finishScreenData.value = data ?: FinishScreenData.NoData
    }

    fun getString(@StringRes resId: Int): String {
        return appContext.resources.getString(resId)
    }
}