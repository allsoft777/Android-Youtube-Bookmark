package com.owllife.youtubebookmark.presentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.domain.CategoryRepository
import com.owllife.youtubebookmark.domain.entity.CategoryEntity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
class MainViewModel constructor(
    appContext: Context,
    private val categoryRepository: CategoryRepository
) : BaseViewModel(appContext) {

    var categoryList: LiveData<List<CategoryEntity>> = MutableLiveData()

    init {
        fetchCategoryFromLocalRepository()
    }

    private fun fetchCategoryFromLocalRepository() {
        dataLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            categoryList = categoryRepository.observeCategories()
        }
    }
}