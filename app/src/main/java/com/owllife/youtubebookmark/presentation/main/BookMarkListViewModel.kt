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
    private var bookmarkList: HashMap<Int, LiveData<List<BookMarkEntity>>> = HashMap()

    private val _dataLoading: HashMap<Int, MutableLiveData<Boolean>> = HashMap()
    val dataLoading: HashMap<Int, MutableLiveData<Boolean>> = _dataLoading

    private var selectedOptionItem: HashMap<Int, MutableLiveData<SelectedBookmarkData>> = HashMap()
    private val _openBookmarkEvent = MutableLiveData<Event<BookMarkEntity>>()
    val openBookmarkEvent: LiveData<Event<BookMarkEntity>> = _openBookmarkEvent

    @Suppress("ReplaceGetOrSet", "ReplacePutWithAssignment")
    fun setDataLoading(categoryId: Int, value: Boolean) {
        if (_dataLoading.get(categoryId) == null) {
            _dataLoading.put(categoryId, MutableLiveData(value))
        } else {
            _dataLoading.get(categoryId)!!.value = value
        }
    }

    @Suppress("ReplaceGetOrSet", "ReplacePutWithAssignment")
    fun getDataLoading(categoryId: Int): LiveData<Boolean> {
        if (_dataLoading.get(categoryId) == null) {
            _dataLoading.put(categoryId, MutableLiveData(false))
        }
        return dataLoading.get(categoryId)!!
    }

    @Suppress("ReplaceGetOrSet", "ReplacePutWithAssignment")
    fun getBookmarkListData(categoryId: Int): LiveData<List<BookMarkEntity>> {
        if (bookmarkList.get(categoryId) == null) {
            bookmarkList.put(categoryId, MutableLiveData())
        }
        return bookmarkList.get(categoryId)!!
    }

    fun fetchDataFromLocalDb(categoryId: Int) {
        setDataLoading(categoryId, true)
        viewModelScope.launch {
            @Suppress("ReplacePutWithAssignment")
            bookmarkList.put(categoryId, bookmarkRepository.observeBookmarks(categoryId))
        }
    }

    @Suppress("ReplaceGetOrSet", "ReplacePutWithAssignment")
    fun getSelectedBookmarkData(categoryId: Int): LiveData<SelectedBookmarkData>? {
        val ret = selectedOptionItem.get(categoryId)
        if (ret == null) {
            val newData = MutableLiveData<SelectedBookmarkData>()
            selectedOptionItem.put(categoryId, newData)
            return newData
        }
        return ret
    }

    fun openBookmarkEvent(bookmark: BookMarkEntity) {
        _openBookmarkEvent.value = Event(bookmark)
    }

    @Suppress("ReplaceGetOrSet")
    fun setSelectedOptionItem(selectedBookmarkData: SelectedBookmarkData) {
        val data = selectedOptionItem.get(selectedBookmarkData.item.categoryId)
        data!!.value = selectedBookmarkData
    }

    @Suppress("ReplaceGetOrSet")
    fun deleteSelectedBookmark(categoryId: Int) {
        setDataLoading(categoryId, true)
        viewModelScope.launch(Dispatchers.Main) {
            bookmarkRepository.deleteBookmark(selectedOptionItem.get(categoryId)!!.value!!.item.id)
            setDataLoading(categoryId, false)
            setToastText(getString(R.string.msg_database_deleted))
        }
    }
}