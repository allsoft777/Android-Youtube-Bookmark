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
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import com.owllife.youtubebookmark.domain.entity.CategoryEntity
import com.owllife.youtubebookmark.domain.resp.YoutubeMovieResp
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
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
) : BaseViewModel(appContext) {

    // two-way data-binding
    var selectedCategory: MutableLiveData<CategoryEntity> = MutableLiveData()
    var movieUrl: MutableLiveData<String> = MutableLiveData()
    var movieData: MutableLiveData<YoutubeMovieResp> = MutableLiveData()

    private var _hideInputMethod: MutableLiveData<Boolean> = MutableLiveData()
    var hideInputMethod: LiveData<Boolean> = _hideInputMethod

    private val _savedToLocalDb: MutableLiveData<Boolean> = MutableLiveData()
    var savedToLocalDb: LiveData<Boolean> = _savedToLocalDb

    private var _categoryList: MutableLiveData<List<CategoryEntity>> = MutableLiveData()
    var categoryList: LiveData<List<CategoryEntity>> = _categoryList

    private var _videoId = String.empty()

    init {
        viewModelScope.launch {
            loadCategoryData()
        }
    }

    private suspend fun loadCategoryData() {
        handleDataLoading(true)
        val result = categoryRepository.queryCategories()
        if (result is Error) {
            setToastText(getString(R.string.msg_failed_to_fetch_category_list))
        } else {
            _categoryList.value = (result as? ResultData.Success)?.data!!
        }
        handleDataLoading(false)
    }

    fun onMovieUrlTextChanged() {
        _hideInputMethod.value = false
    }

    fun handleQueryBtn() {
        movieUrl.value =
            "https://youtu.be/ywDBAKs3DFA" //"https://www.youtube.com/watch?v=MsGBOss88iw"
        if (movieUrl.value.isNullOrEmpty()) {
            setToastText(getString(R.string.msg_input_youtube_url))
            return
        }

        _videoId = String.empty()
        if (movieUrl.value!!.contains("youtu.be/")) {
            val split = movieUrl.value!!.split("/")
            _videoId = split[split.size - 1]
        } else if (movieUrl.value!!.contains("watch?v=")) {
            val split = movieUrl.value!!.split("watch?v=")
            if (split.size == 2) {
                _videoId = split[split.size - 1]
            }
        }
        if (_videoId.isNullOrEmpty()) {
            setToastText(getString(R.string.msg_input_youtube_url_correctly))
            return
        }

        _hideInputMethod.value = true
        handleDataLoading(true)

        viewModelScope.launch {
            val resp = youtubeRemoteRepository.getMovieInfo(_videoId)
            if (resp is ResultData.Success) {
                val data = resp.data
                movieData.value = data
            } else if (resp is ResultData.Failure) {
                setToastText(resp.exception.message!!)
            }
            handleDataLoading(false)
        }
    }

    fun setSelectedCategory(pos: Int) {
        selectedCategory.value = categoryList.value?.get(pos);
    }

    fun saveToLocalDb() {
        if (selectedCategory.value == null) {
            setToastText(getString(R.string.msg_selected_category))
            return
        }
        handleDataLoading(true)

        @Suppress("SENSELESS_COMPARISON")
        viewModelScope.launch {
            val categoryId = selectedCategory.value!!.id
            val videoId = _videoId
            var thumbnailUrl = ""
            if (movieData.value!!.items[0].snippet.thumbnails.high != null) {
                thumbnailUrl = movieData.value!!.items[0].snippet.thumbnails.high.url
            }
            if (thumbnailUrl.isEmpty() && movieData.value!!.items[0].snippet.thumbnails.medium != null) {
                thumbnailUrl = movieData.value!!.items[0].snippet.thumbnails.medium.url
            }
            if (thumbnailUrl.isEmpty()) {
                thumbnailUrl = movieData.value!!.items[0].snippet.thumbnails.default.url
            }

            val title = movieData.value!!.items[0].snippet.title
            val desc = movieData.value!!.items[0].snippet.description
            val tagsList = movieData.value!!.items[0].snippet.tags
            val sb = StringBuilder()
            for (i in tagsList.indices) {
                if (i != 0) {
                    sb.append(",")
                }
                sb.append(tagsList[i])
            }
            val tagsStr = sb.toString()
            val channelId = movieData.value!!.items[0].snippet.channelId
            val publishedAt = movieData.value!!.items[0].snippet.publishedAt

            val bookMarkEntity = BookMarkEntity(
                categoryId = categoryId,
                videoId = videoId,
                thumbnailUrl = thumbnailUrl,
                title = title,
                description = desc,
                tags = tagsStr,
                channelId = channelId,
                publishedAt = publishedAt,
                bookmarked_at = System.currentTimeMillis()
            )

            bookmarkRepository.insertNewBookmark(bookMarkEntity)
            handleDataLoading(false)
            _savedToLocalDb.value = true
            setToastText(getString(R.string.msg_database_inserted))
        }
    }
}