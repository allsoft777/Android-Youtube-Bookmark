package com.owllife.youtubebookmark.presentation.category

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.owllife.youtubebookmark.domain.entity.CategoryEntity
import com.owllife.youtubebookmark.presentation.data.SelectedCategoryData

/**
 * @author owllife.dev
 * @since 20. 6. 5
 */

@BindingAdapter("category_list")
fun setCategoryList(listView: RecyclerView, items: List<CategoryEntity>?) {
    if (items.isNullOrEmpty() || listView.adapter == null) {
        return
    }
    (listView.adapter as CategoryAdapter).submitList(items)
}

@BindingAdapter(value = ["viewmodel", "item"], requireAll = true)
fun setSelectedOptionItem(view: View, viewModel: EditCategoryViewModel, item: CategoryEntity) {
    view.setOnClickListener {
        val data = SelectedCategoryData(view, item)
        viewModel.selectedOptionItem.value = data
    }
}

fun configureListView(viewModel: EditCategoryViewModel, categoryListView: RecyclerView) {
    val listAdapter = CategoryAdapter(viewModel)
    categoryListView.adapter = listAdapter
}