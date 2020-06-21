package com.owllife.youtubebookmark.presentation.main

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import com.owllife.youtubebookmark.presentation.data.SelectedBookmarkData

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
@BindingAdapter("bookmark_list")
fun setBookmarkList(listView: RecyclerView, items: List<BookMarkEntity>?) {
    if (items == null || listView.adapter == null) {
        return
    }
    (listView.adapter as BookMarkListAdapter).submitList(items)
}

@BindingAdapter("bookmark_thumbnail")
fun renderThumbnail(view: ImageView, data: BookMarkEntity?) {
    if (data == null) {
        return
    }
    Glide.with(view.context)
        .load(data.thumbnailUrl)
        .into(view)
}

@BindingAdapter(value = ["bookmark_viewmodel", "bookmark_item"], requireAll = true)
fun setSelectedBookmarkOption(
    view: View, viewModel: BookMarkListViewModel,
    item: BookMarkEntity
) {
    view.setOnClickListener {
        val data = SelectedBookmarkData(view, item)
        viewModel.setSelectedOptionItem(data)
    }
}
