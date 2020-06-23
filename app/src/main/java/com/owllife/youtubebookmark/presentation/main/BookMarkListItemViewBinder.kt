package com.owllife.youtubebookmark.presentation.main

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.owllife.youtubebookmark.core.loadFromUrl
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import com.owllife.youtubebookmark.presentation.data.SelectedBookmarkData

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
@BindingAdapter("bookmark_list_item_thumbnail")
fun renderThumbnail(view: ImageView, data: BookMarkEntity?) {
    if (data == null) {
        return
    }
    view.loadFromUrl(data.thumbnailUrl)
}

@BindingAdapter(
    value = ["bookmark_list_item_viewmodel", "bookmark_list_item"],
    requireAll = true
)
fun setSelectedBookmarkOption(
    view: View,
    viewModel: BookMarkListViewModel,
    item: BookMarkEntity
) {
    view.setOnClickListener {
        val data = SelectedBookmarkData(view, item)
        viewModel.setSelectedOptionItem(data)
    }
}