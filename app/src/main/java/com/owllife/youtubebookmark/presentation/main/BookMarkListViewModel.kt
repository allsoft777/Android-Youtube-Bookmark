package com.owllife.youtubebookmark.presentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.Event
import com.owllife.youtubebookmark.domain.BookmarkRepository
import com.owllife.youtubebookmark.entity.BookMarkSimpleVO
import com.owllife.youtubebookmark.presentation.data.SelectedBookmarkData
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */

typealias SelectedOptionItem = MutableLiveData<Event<SelectedBookmarkData>>

@Suppress("ReplaceGetOrSet", "ReplacePutWithAssignment")
class BookMarkListViewModel constructor(
    private val appContext: Context,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    private var _bookmarkList: HashMap<Int, MutableLiveData<List<BookMarkSimpleVO>>> = HashMap()
    private val _dataLoading: HashMap<Int, MutableLiveData<Boolean>> = HashMap()
    private var _selectedOptionItem: HashMap<Int, SelectedOptionItem> = HashMap()

    private val _toastText: MutableLiveData<String> = MutableLiveData()
    val toastText: LiveData<String> get() = _toastText

    fun setDataLoading(categoryId: Int, value: Boolean) {
        if (_dataLoading.get(categoryId) == null) {
            _dataLoading.put(categoryId, MutableLiveData(value))
        } else {
            _dataLoading.get(categoryId)!!.value = value
        }
    }

    fun getDataLoading(categoryId: Int): LiveData<Boolean> {
        if (_dataLoading.get(categoryId) == null) {
            _dataLoading.put(categoryId, MutableLiveData())
        }
        return _dataLoading.get(categoryId)!!
    }

    fun getBookmarkListData(categoryId: Int): MutableLiveData<List<BookMarkSimpleVO>> {
        if (_bookmarkList.get(categoryId) == null) {
            _bookmarkList.put(categoryId, MutableLiveData())
        }
        return _bookmarkList.get(categoryId)!!
    }

    fun fetchDataFromLocalDb(categoryId: Int) {
        viewModelScope.launch {
            setDataLoading(categoryId, true)
            val data = bookmarkRepository.fetchBookMarksSimpleType(categoryId)
            getBookmarkListData(categoryId).value = data
        }
    }

    fun getSelectedBookmarkData(categoryId: Int): LiveData<Event<SelectedBookmarkData>>? {
        val ret = _selectedOptionItem.get(categoryId)
        if (ret == null) {
            val newData = SelectedOptionItem()
            _selectedOptionItem.put(categoryId, newData)
            return newData
        }
        return ret
    }

    fun setSelectedOptionItem(selectedBookmarkData: SelectedBookmarkData) {
        val data = _selectedOptionItem.get(selectedBookmarkData.item.categoryId)
        data!!.value = Event(selectedBookmarkData)
    }

    fun deleteSelectedBookmark(categoryId: Int) {
        viewModelScope.launch {
            setDataLoading(categoryId, true)
            bookmarkRepository.deleteBookmark(
                _selectedOptionItem.get(categoryId)!!
                    .value!!.peekContent().item.id
            )
            setDataLoading(categoryId, false)
            _toastText.value = appContext.getString(R.string.msg_database_deleted)
            fetchDataFromLocalDb(categoryId)
        }
    }
}