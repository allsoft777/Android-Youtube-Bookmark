package com.owllife.youtubebookmark.presentation.main

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
fun renderBookmarkList(listView: RecyclerView, items: List<BookMarkEntity>?) {
    if (listView.adapter == null || items.isNullOrEmpty()) {
        listView.gone()
        return
    }
    (listView.adapter as BookMarkListAdapter).submitList(items)
    listView.visible()
}