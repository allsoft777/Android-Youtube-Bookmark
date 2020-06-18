package com.owllife.youtubebookmark.presentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.core.Event
import com.owllife.youtubebookmark.domain.BookmarkRepository
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
class BookMarkListViewModel constructor(
    appContext: Context,
    private val bookmarkRepository: BookmarkRepository
) : BaseViewModel(appContext) {

    var bookmarkList: HashMap<Int, LiveData<List<BookMarkEntity>>> = HashMap()

    private val _openBookmarkEvent = MutableLiveData<Event<BookMarkEntity>>()
    val openBookmarkEvent: LiveData<Event<BookMarkEntity>> = _openBookmarkEvent

    fun fetchDataFromLocalDb(categoryId: Int) {
        dataLoading.value = true
        viewModelScope.launch {
            @Suppress("ReplacePutWithAssignment")
            bookmarkList.put(categoryId, bookmarkRepository.observeBookmarks(categoryId))
        }
    }

    fun openBookmarkEvent(bookmark: BookMarkEntity) {
        _openBookmarkEvent.value = Event(bookmark)
    }
}