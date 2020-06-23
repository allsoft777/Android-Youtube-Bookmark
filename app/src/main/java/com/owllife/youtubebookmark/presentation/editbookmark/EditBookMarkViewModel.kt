package com.owllife.youtubebookmark.presentation.editbookmark

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.empty
import com.owllife.youtubebookmark.domain.BookmarkRepository
import com.owllife.youtubebookmark.domain.CategoryRepository
import com.owllife.youtubebookmark.domain.ResultData
import com.owllife.youtubebookmark.domain.YoutubeRemoteRepository
import com.owllife.youtubebookmark.domain.entity.CategoryEntity
import com.owllife.youtubebookmark.domain.resp.YoutubeVideoResp
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
class EditBookMarkViewModel(
    appContext: Context,
    private val categoryRepository: CategoryRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val youtubeRemoteRepository: YoutubeRemoteRepository
) : BaseViewModel(appContext) {

    // two-way data binding
    var movieUrl: MutableLiveData<String> = MutableLiveData()

    private var _selectedCategory: MutableLiveData<CategoryEntity> = MutableLiveData()
    var selectedCategory: LiveData<CategoryEntity> = _selectedCategory

    private var _movieData: MutableLiveData<YoutubeVideoResp> = MutableLiveData()
    var movieData: LiveData<YoutubeVideoResp> = _movieData

    private var _categoryManagement: MutableLiveData<Boolean> = MutableLiveData()
    var categoryManagement: LiveData<Boolean> = _categoryManagement

    private var _hideInputMethod: MutableLiveData<Boolean> = MutableLiveData()
    var hideInputMethod: LiveData<Boolean> = _hideInputMethod

    private val _savedToLocalDb: MutableLiveData<Boolean> = MutableLiveData()
    var savedToLocalDb: LiveData<Boolean> = _savedToLocalDb

    private var _categoryList: MutableLiveData<List<CategoryEntity>> = MutableLiveData()
    var categoryList: LiveData<List<CategoryEntity>> = _categoryList

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private var _videoId = String.empty()

    init {
        loadCategoryData()
    }

    private fun loadCategoryData() {
        viewModelScope.launch(Dispatchers.IO) {
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
            setToastText(getString(R.string.msg_input_youtube_url))
            return
        }
        _videoId = extractYouTubeVideoIdFromUrl(movieUrl.value!!)
        if (_videoId.isEmpty()) {
            setToastText(getString(R.string.msg_input_youtube_url_correctly))
            return
        }

        _hideInputMethod.value = true
        _dataLoading.value = true

        viewModelScope.launch {
            val resp = youtubeRemoteRepository.getMovieInfo(_videoId)
            if (resp is ResultData.Success) {
                val data = resp.data
                _movieData.value = data
            } else if (resp is ResultData.Failure) {
                setToastText(resp.exception.message!!)
            }
            _dataLoading.value = false
        }
    }

    fun setSelectedCategory(pos: Int) {
        _selectedCategory.value = categoryList.value?.get(pos);
    }

    fun saveToLocalDb() {
        if (selectedCategory.value == null) {
            setToastText(getString(R.string.msg_selected_category))
            return
        }
        _dataLoading.value = true

        viewModelScope.launch {
            val bookMarkEntity = mapYoutubeVideoRespToBookMarkEntity(movieData.value!!)
            bookMarkEntity.categoryId = _selectedCategory.value!!.id
            bookMarkEntity.videoId = _videoId
            bookmarkRepository.insertNewBookmark(bookMarkEntity)

            _dataLoading.value = false
            _savedToLocalDb.value = true
            setToastText(getString(R.string.msg_database_inserted))
        }
    }
}