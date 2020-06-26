package com.owllife.youtubebookmark.presentation.category

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.owllife.youtubebookmark.core.gone
import com.owllife.youtubebookmark.core.visible
import com.owllife.youtubebookmark.entity.CategoryEntireVO

/**
 * @author owllife.dev
 * @since 20. 6. 5
 */

@BindingAdapter("category_listview_items")
fun setCategoryList(listView: RecyclerView, items: List<CategoryEntireVO>?) {
    if (items.isNullOrEmpty() || listView.adapter == null) {
        listView.gone()
        return
    }
    (listView.adapter as CategoryAdapter).submitList(items)
    listView.visible()
}