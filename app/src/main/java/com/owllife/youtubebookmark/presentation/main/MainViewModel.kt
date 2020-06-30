package com.owllife.youtubebookmark.presentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.domain.CategoryRepository
import com.owllife.youtubebookmark.domain.SharedPref
import com.owllife.youtubebookmark.entity.CategoryEntireVO
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
class MainViewModel constructor(
    appContext: Context,
    private val categoryRepository: CategoryRepository,
    sharedPref: SharedPref
) : BaseViewModel(appContext) {

    var categoryList: LiveData<List<CategoryEntireVO>> = MutableLiveData()

    private var _viewType: MutableLiveData<Int> = MutableLiveData()
    var viewType: LiveData<Int> = _viewType

    init {
        _viewType.value = sharedPref.getViewType()
        fetchCategoryFromLocalRepository()
    }

    private fun fetchCategoryFromLocalRepository() {
        viewModelScope.launch {
            categoryList = categoryRepository.observeCategories()
        }
    }

    val setSelectedViewType: (Int) -> Unit = { type ->
        sharedPref.setViewType(type)
        _viewType.value = type
    }
}