package com.owllife.youtubebookmark.presentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.R
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
@Suppress("ReplaceGetOrSet", "ReplacePutWithAssignment")
class BookMarkListViewModel constructor(
    appContext: Context,
    private val bookmarkRepository: BookmarkRepository
) : BaseViewModel(appContext) {

    private var _bookmarkList: HashMap<Int, MutableLiveData<List<BookMarkEntity>>> = HashMap()
    private val _dataLoading: HashMap<Int, MutableLiveData<Boolean>> = HashMap()
    private var _selectedOptionItem: HashMap<Int, MutableLiveData<SelectedBookmarkData>> = HashMap()

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

    fun getBookmarkListData(categoryId: Int): MutableLiveData<List<BookMarkEntity>> {
        if (_bookmarkList.get(categoryId) == null) {
            _bookmarkList.put(categoryId, MutableLiveData())
        }
        return _bookmarkList.get(categoryId)!!
    }

    fun fetchDataFromLocalDb(categoryId: Int) {
        setDataLoading(categoryId, true)
        viewModelScope.launch {
            val data = bookmarkRepository.fetchBookmarks(categoryId)
            getBookmarkListData(categoryId).value = data
        }
    }

    fun getSelectedBookmarkData(categoryId: Int): LiveData<SelectedBookmarkData>? {
        val ret = _selectedOptionItem.get(categoryId)
        if (ret == null) {
            val newData = MutableLiveData<SelectedBookmarkData>()
            _selectedOptionItem.put(categoryId, newData)
            return newData
        }
        return ret
    }

    fun setSelectedOptionItem(selectedBookmarkData: SelectedBookmarkData) {
        val data = _selectedOptionItem.get(selectedBookmarkData.item.categoryId)
        data!!.value = selectedBookmarkData
    }

    fun deleteSelectedBookmark(categoryId: Int) {
        setDataLoading(categoryId, true)
        viewModelScope.launch(Dispatchers.Main) {
            bookmarkRepository.deleteBookmark(_selectedOptionItem.get(categoryId)!!.value!!.item.id)
            setDataLoading(categoryId, false)
            setToastText(getString(R.string.msg_database_deleted))
            fetchDataFromLocalDb(categoryId)
        }
    }
}