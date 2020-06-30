package com.owllife.youtubebookmark.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.domain.CategoryRepository
import com.owllife.youtubebookmark.domain.SharedPref
import com.owllife.youtubebookmark.entity.CategoryEntireVO
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
class MainViewModel constructor(
    private val categoryRepository: CategoryRepository,
    private val sharedPref: SharedPref
) : ViewModel() {

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