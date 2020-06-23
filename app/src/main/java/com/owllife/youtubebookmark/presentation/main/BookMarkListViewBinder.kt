package com.owllife.youtubebookmark.presentation.main

import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.owllife.youtubebookmark.core.gone
import com.owllife.youtubebookmark.core.visible
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
@BindingAdapter("bookmark_listview_items")
fun setBookmarkList(listView: RecyclerView, items: List<BookMarkEntity>?) {
    if (items == null || listView.adapter == null) {
        return
    }
    (listView.adapter as BookMarkListAdapter).submitList(items)
}

@BindingAdapter("listview_visibility")
fun setListViewVisibility(
    view: RecyclerView, items: List<BookMarkEntity>?
) {
    if (items.isNullOrEmpty()) {
        view.gone()
    } else {
        view.visible()
    }
}

@BindingAdapter("progressbar_visibility")
fun setProgressBarVisibility(
    view: ContentLoadingProgressBar,
    isLoading: Boolean
) {
    when (isLoading) {
        true -> view.visible()
        false -> view.gone()
    }
}

@BindingAdapter("no_items_visibility")
fun setNoItemsVisibility(
    view: TextView,
    items: List<BookMarkEntity>?
) {
    when (items.isNullOrEmpty()) {
        true -> view.visible()
        false -> view.gone()
    }
}