package com.owllife.youtubebookmark.presentation.category

import android.view.View
import androidx.databinding.BindingAdapter
import com.owllife.youtubebookmark.domain.entity.CategoryEntity
import com.owllife.youtubebookmark.presentation.data.SelectedCategoryData

/**
 * @author owllife.dev
 * @since 20. 6. 24
 */
@BindingAdapter(value = ["category_item_viewmodel", "category_item"], requireAll = true)
fun setSelectedOptionItem(view: View, viewModel: EditCategoryViewModel, item: CategoryEntity) {
    view.setOnClickListener {
        val data = SelectedCategoryData(view, item)
        viewModel.selectedOptionItem.value = data
    }
}
