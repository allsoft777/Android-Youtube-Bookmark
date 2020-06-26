package com.owllife.youtubebookmark.presentation.main

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.owllife.youtubebookmark.core.loadFromUrl
import com.owllife.youtubebookmark.entity.BookMarkSimpleVO

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
@BindingAdapter("bookmark_list_item_thumbnail")
fun renderThumbnail(view: ImageView, data: BookMarkSimpleVO?) {
    if (data == null) {
        return
    }
    view.loadFromUrl(data.thumbnailUrl)
}