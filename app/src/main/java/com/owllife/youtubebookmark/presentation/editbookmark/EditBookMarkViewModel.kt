package com.owllife.youtubebookmark.presentation.editbookmark

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.empty
import com.owllife.youtubebookmark.domain.BookmarkRepository
import com.owllife.youtubebookmark.domain.CategoryRepository
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.YoutubeRemoteRepository
import com.owllife.youtubebookmark.domain.resp.YoutubeVideoResp
import com.owllife.youtubebookmark.entity.CategoryEntireVO
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
class EditBookMarkViewModel(
    private val appContext: Context,
    private val categoryRepository: CategoryRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val youtubeRemoteRepository: YoutubeRemoteRepository
) : ViewModel() {

    // two-way data binding
    var movieUrl: MutableLiveData<String> = MutableLiveData()

    private var _selectedCategory: MutableLiveData<CategoryEntireVO> = MutableLiveData()
    var selectedCategory: LiveData<CategoryEntireVO> = _selectedCategory

    private var _movieData: MutableLiveData<YoutubeVideoResp> = MutableLiveData()
    var movieData: LiveData<YoutubeVideoResp> = _movieData

    private var _categoryManagement: MutableLiveData<Boolean> = MutableLiveData()
    var categoryManagement: LiveData<Boolean> = _categoryManagement

    private var _hideInputMethod: MutableLiveData<Boolean> = MutableLiveData()
    var hideInputMethod: LiveData<Boolean> = _hideInputMethod

    private val _savedToLocalDb: MutableLiveData<Boolean> = MutableLiveData()
    var savedToLocalDb: LiveData<Boolean> = _savedToLocalDb

    private var _categoryList: MutableLiveData<List<CategoryEntireVO>> = MutableLiveData()
    var categoryList: LiveData<List<CategoryEntireVO>> = _categoryList

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _toastText: MutableLiveData<String> = MutableLiveData()
    val toastText: LiveData<String> get() = _toastText

    private var _videoId = String.empty()

    init {
        loadCategoryData()
    }

    private fun loadCategoryData() {
        viewModelScope.launch {
            categoryList = categoryRepository.observeCategories()
        }
    }

    fun onMovieUrlTextChanged() {
        _hideInputMethod.value = false
    }

    fun handleCategoryManagement() {
        _categoryManagement.value = true
    }

    fun queryYouTubeVideoInfo() {
        movieUrl.value = "https://www.youtube.com/watch?v=4bmUFRxNEIg"
        if (movieUrl.value.isNullOrEmpty()) {
            _toastText.value = appContext.getString(R.string.msg_input_youtube_url)
            return
        }
        _videoId = extractYouTubeVideoIdFromUrl(movieUrl.value!!)
        if (_videoId.isEmpty()) {
            _toastText.value = appContext.getString(R.string.msg_input_youtube_url_correctly)
            return
        }
        _hideInputMethod.value = true

        viewModelScope.launch {
            _dataLoading.value = true
            val resp = youtubeRemoteRepository.getMovieInfo(_videoId)
            if (resp is ResultData.Success) {
                val data = resp.data
                _movieData.value = data
            } else if (resp is ResultData.Failure) {
                _toastText.value = resp.exception.message!!
            }
            _dataLoading.value = false
        }
    }

    fun setSelectedCategory(pos: Int) {
        _selectedCategory.value = categoryList.value?.get(pos);
    }

    fun saveToLocalDb() {
        if (selectedCategory.value == null) {
            _toastText.value = appContext.getString(R.string.msg_selected_category)
            return
        }

        viewModelScope.launch {
            _dataLoading.value = true
            val bookMarkEntity = mapYoutubeVideoRespToBookMarkEntity(movieData.value!!)
            bookMarkEntity.categoryId = _selectedCategory.value!!.id
            bookMarkEntity.videoId = _videoId
            bookmarkRepository.insertNewBookmark(bookMarkEntity)

            _dataLoading.value = false
            _savedToLocalDb.value = true
            _toastText.value = appContext.getString(R.string.msg_database_inserted)
        }
    }
}