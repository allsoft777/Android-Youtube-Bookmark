package com.owllife.youtubebookmark.presentation.main

import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.owllife.youtubebookmark.R
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

@BindingAdapter(value = ["bookmark_listview_items", "bookmark_listview_loading"], requireAll = true)
fun setListViewVisibility(
    view: RelativeLayout, items: List<BookMarkEntity>?, isLoading: Boolean
) {
    val listView: RecyclerView = view.findViewById(R.id.bookmark_listview)
    val emptyTv: TextView = view.findViewById(R.id.empty_text)
    val loadingView: ContentLoadingProgressBar = view.findViewById(R.id.progressbar)
    if (isLoading) {
        loadingView.visible()
        listView.gone()
        emptyTv.gone()
        return
    }
    if (items!!.isEmpty()) {
        emptyTv.visible()
        loadingView.gone()
        listView.gone()
        return
    }
    emptyTv.gone()
    loadingView.gone()
    listView.visible()
}