package com.owllife.youtubebookmark.presentation.editbookmark

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.owllife.youtubebookmark.domain.resp.YoutubeMovieResp

/**
 * @author owllife.dev
 * @since 20. 6. 12
 */
@BindingAdapter("thumbnail")
fun renderThumbnail(view: ImageView, data: YoutubeMovieResp?) {
    if (data == null || data.items.isEmpty()) {
        return
    }

    val thumbnails = data.items[0].snippet.thumbnails
    var url = thumbnails.maxres.url
    if (url.isEmpty() && thumbnails.high.url.isNotEmpty()) {
        url = thumbnails.high.url
    }

    Glide.with(view.context)
        .load(url)
        .into(view)
}