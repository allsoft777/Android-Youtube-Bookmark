package com.owllife.youtubebookmark.presentation.main

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity

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

@BindingAdapter("thumbnail")
fun renderThumbnail(view: ImageView, data: BookMarkEntity?) {
    if (data == null) {
        return
    }
    Glide.with(view.context)
        .load(data.thumbnailUrl)
        .into(view)
}