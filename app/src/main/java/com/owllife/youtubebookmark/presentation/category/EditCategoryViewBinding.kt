package com.owllife.youtubebookmark.presentation.category

import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.gone
import com.owllife.youtubebookmark.core.visible
import com.owllife.youtubebookmark.domain.entity.CategoryEntity

/**
 * @author owllife.dev
 * @since 20. 6. 5
 */

@BindingAdapter("category_listview_items")
fun setCategoryList(listView: RecyclerView, items: List<CategoryEntity>?) {
    if (items.isNullOrEmpty() || listView.adapter == null) {
        return
    }
    (listView.adapter as CategoryAdapter).submitList(items)
}

@BindingAdapter(value = ["category_listview_items", "category_listview_loading"], requireAll = true)
fun setListViewVisibility(
    view: RelativeLayout, items: List<CategoryEntity>?, isLoading: Boolean
) {
    val listView: RecyclerView = view.findViewById(R.id.category_list_view)
    val emptyTv: TextView = view.findViewById(R.id.empty_text)
    val loadingView: ContentLoadingProgressBar = view.findViewById(R.id.progressbar)
    if (isLoading) {
        loadingView.visible()
        listView.gone()
        emptyTv.gone()
        return
    }
    if (items.isNullOrEmpty()) {
        emptyTv.visible()
        loadingView.gone()
        listView.gone()
        return
    }
    emptyTv.gone()
    loadingView.gone()
    listView.visible()
}