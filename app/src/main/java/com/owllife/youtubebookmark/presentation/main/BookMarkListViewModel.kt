package com.owllife.youtubebookmark.presentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.Event
import com.owllife.youtubebookmark.domain.BookmarkRepository
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import com.owllife.youtubebookmark.presentation.data.SelectedBookmarkData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
class BookMarkListViewModel constructor(
    appContext: Context,
    private val bookmarkRepository: BookmarkRepository
) : BaseViewModel(appContext) {

    // Two-way data binding
    var bookmarkList: HashMap<Int, LiveData<List<BookMarkEntity>>> = HashMap()

    private var selectedOptionItem: HashMap<Int, MutableLiveData<SelectedBookmarkData>> = HashMap()
    private val _openBookmarkEvent = MutableLiveData<Event<BookMarkEntity>>()
    val openBookmarkEvent: LiveData<Event<BookMarkEntity>> = _openBookmarkEvent
    var categoryId: Int? = null

    fun fetchDataFromLocalDb(categoryId: Int) {
        this.categoryId = categoryId
        dataLoading.value = true
        viewModelScope.launch {
            @Suppress("ReplacePutWithAssignment")
            bookmarkList.put(categoryId, bookmarkRepository.observeBookmarks(categoryId))
        }
    }

    fun getSelectedBookmarkData(): LiveData<SelectedBookmarkData>? {
        val ret = selectedOptionItem.get(categoryId)
        if (ret == null) {
            val newData = MutableLiveData<SelectedBookmarkData>()
            @Suppress("ReplacePutWithAssignment")
            selectedOptionItem.put(categoryId!!, newData)
            return newData
        }
        return ret
    }

    fun openBookmarkEvent(bookmark: BookMarkEntity) {
        _openBookmarkEvent.value = Event(bookmark)
    }

    fun setSelectedOptionItem(selectedBookmarkData: SelectedBookmarkData) {
        @Suppress("ReplaceGetOrSet")
        val data = selectedOptionItem.get(categoryId!!)
        data!!.value = selectedBookmarkData
    }

    fun deleteSelectedBookmark() {
        handleDataLoading(true)
        viewModelScope.launch(Dispatchers.Main) {
            @Suppress("ReplaceGetOrSet")
            bookmarkRepository.deleteBookmark(selectedOptionItem.get(categoryId)!!.value!!.item.id)
            handleDataLoading(false)
            setToastText(getString(R.string.msg_database_deleted))
        }
    }
}