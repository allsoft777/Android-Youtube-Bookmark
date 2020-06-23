package com.owllife.youtubebookmark.presentation.editbookmark

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.owllife.youtubebookmark.core.loadFromUrl
import com.owllife.youtubebookmark.domain.entity.CategoryEntity
import com.owllife.youtubebookmark.domain.resp.YoutubeVideoResp

/**
 * @author owllife.dev
 * @since 20. 6. 12
 */
@BindingAdapter("thumbnail")
fun renderThumbnail(view: ImageView, data: YoutubeVideoResp?) {
    if (data == null || data.items.isEmpty()) {
        return
    }
    val thumbnails = data.items[0].snippet.thumbnails
    var url = thumbnails.maxres?.url
    if (url.isNullOrEmpty() && thumbnails.high != null) {
        url = thumbnails.high.url
    }
    view.loadFromUrl(url)
}

@BindingAdapter(value = ["selected_category", "youtube_video_resp"], requireAll = true)
fun renderSaveBtnEnableState(
    view: TextView,
    selectedCategory: CategoryEntity?,
    youtubeData: YoutubeVideoResp?
) {
    if (selectedCategory == null || youtubeData == null) {
        view.isEnabled = false
        return
    }
    view.isEnabled = selectedCategory.name.isNotEmpty() && youtubeData.items.isNotEmpty()
}