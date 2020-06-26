package com.owllife.youtubebookmark.presentation.category

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.empty
import com.owllife.youtubebookmark.data.logger.MainLogger
import com.owllife.youtubebookmark.domain.CategoryRepository
import com.owllife.youtubebookmark.domain.entity.CategoryEntity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import com.owllife.youtubebookmark.presentation.data.SelectedCategoryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author owllife.dev
 * @since 20. 6. 3
 */
class EditCategoryViewModel constructor(
    private val appContext: Context,
    private val categoryRepository: CategoryRepository
) : BaseViewModel(appContext) {

    companion object {
        private val TAG = EditCategoryViewModel::class.java.simpleName
    }

    // Two-way dataBinding, exposing MutableLiveData
    var newCategoryName: MutableLiveData<String> = MutableLiveData()
    var editingCategoryEntity: MutableLiveData<CategoryEntity> = MutableLiveData()
    var selectedOptionItem: MutableLiveData<SelectedCategoryData> = MutableLiveData()
    var categoryList: LiveData<List<CategoryEntity>> = MutableLiveData()

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val dataLoading: MutableLiveData<Boolean> = _dataLoading

    init {
        fetchCategoryFromLocalRepository()
    }

    override fun onCleared() {
        super.onCleared()
        MainLogger.d(TAG, "onCleared")
    }

    private fun fetchCategoryFromLocalRepository() {
        _dataLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            categoryList = categoryRepository.observeCategories()
        }
    }

    fun insertNewCategory() = viewModelScope.launch {
        newCategoryName.value?.let {
            val order =
                if (categoryList.value.isNullOrEmpty()) 0 else categoryList.value!!.size.plus(1)
            val entity = CategoryEntity(
                order,
                newCategoryName.value!!
            )
            categoryRepository.insertNewCategory(entity)
            newCategoryName.value = String.empty()
            setToastText(getString(R.string.msg_database_inserted))
        }
    }

    fun updateEditingCategoryName() = viewModelScope.launch {
        editingCategoryEntity.value?.let {
            categoryRepository.updateCategory(it)
            setToastText(getString(R.string.msg_database_updated))
        }
    }

    @SuppressLint("CheckResult")
    fun deleteCategory() = viewModelScope.launch {
        val cnt = categoryRepository.deleteCategory(selectedOptionItem.value?.item!!.id)
        val msg = appContext.getString(R.string.msg_category_deleted, cnt)
        setToastText(msg)
    }

    fun setEditingCategory() {
        editingCategoryEntity.value = CategoryEntity.copy(selectedOptionItem.value?.item!!)
    }
}