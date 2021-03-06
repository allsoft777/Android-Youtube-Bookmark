package com.owllife.youtubebookmark.presentation.player

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.domain.BookmarkRepository
import com.owllife.youtubebookmark.entity.BookMarkEntireVO
import com.owllife.youtubebookmark.presentation.util.PresentationConstants
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
class YoutubePlayerViewModel(
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    private var _videoData: MutableLiveData<BookMarkEntireVO> = MutableLiveData()
    var videoData: LiveData<BookMarkEntireVO> = _videoData

    private var _showUiController: MutableLiveData<Boolean> = MutableLiveData(true)
    var showUiController: LiveData<Boolean> = _showUiController

    private var _pipMode: MutableLiveData<Boolean> = MutableLiveData(false)
    var pipMode: LiveData<Boolean> = _pipMode

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: LiveData<Boolean> = _dataLoading

    fun loadData(intent: Intent) {
        viewModelScope.launch {
            _dataLoading.value = true
            val dbId = intent.getIntExtra(PresentationConstants.KEY_DB_ID, -1)
            val data = bookmarkRepository.fetchBookMarkEntireType(dbId)
            _videoData.value = data
            _dataLoading.value = false
        }
    }

    fun setShowUiController(show: Boolean) {
        _showUiController.value = show
    }

    fun setPipMode(isPipMode: Boolean) {
        _pipMode.value = isPipMode
    }
}