package com.owllife.youtubebookmark.presentation.player

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.domain.BookmarkRepository
import com.owllife.youtubebookmark.entity.BookMarkEntireVO
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import com.owllife.youtubebookmark.presentation.util.PresentationConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
class YoutubePlayerViewModel(
    appContext: Context,
    private val bookmarkRepository: BookmarkRepository
) : BaseViewModel(appContext) {

    private var _entity: MutableLiveData<BookMarkEntireVO> = MutableLiveData()
    var entity: LiveData<BookMarkEntireVO> = _entity

    private var _showUiController: MutableLiveData<Boolean> = MutableLiveData(true)
    var showUiController: LiveData<Boolean> = _showUiController

    private var _isPipMode: MutableLiveData<Boolean> = MutableLiveData(false)
    var isPipMode: LiveData<Boolean> = _isPipMode

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun loadData(intent: Intent) {
        _dataLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val dbId = intent.getIntExtra(PresentationConstants.KEY_DB_ID, -1)
            val data = bookmarkRepository.fetchBookMarkEntireType(dbId)
            _entity.postValue(data)
            _dataLoading.postValue(false)
        }
    }

    fun setShowUiController(show: Boolean) {
        _showUiController.value = show
    }

    fun setPipMode(isPipMode: Boolean) {
        _isPipMode.value = isPipMode
    }
}