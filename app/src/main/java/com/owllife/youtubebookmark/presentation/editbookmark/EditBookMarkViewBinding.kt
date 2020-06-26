package com.owllife.youtubebookmark.presentation.editbookmark

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.gone
import com.owllife.youtubebookmark.core.loadFromUrl
import com.owllife.youtubebookmark.core.visible
import com.owllife.youtubebookmark.domain.resp.YoutubeVideoResp
import com.owllife.youtubebookmark.entity.CategoryEntireVO

/**
 * @author owllife.dev
 * @since 20. 6. 12
 */
@BindingAdapter(
    value = ["edit_bookmark_selected_category", "edit_bookmark_youtube_video_resp"],
    requireAll = true
)
fun renderSaveBtnEnableState(
    view: TextView,
    selectedCategory: CategoryEntireVO?,
    youtubeData: YoutubeVideoResp?
) {
    if (selectedCategory == null || youtubeData == null) {
        view.isEnabled = false
        return
    }
    view.isEnabled = selectedCategory.name.isNotEmpty() && youtubeData.items.isNotEmpty()
}

@Suppress("ReplaceGetOrSet")
@BindingAdapter("youtube_resp_data")
fun renderYoutubeResp(view: LinearLayout, resp: YoutubeVideoResp?) {
    if (resp == null || resp.items.isNullOrEmpty()) {
        view.gone()
        return
    }
    view.visible()

    // render title
    (view.findViewById(R.id.title) as TextView).text = resp.items.get(0).snippet!!.title

    // render thumbnail
    val thumbnails = resp.items[0].snippet!!.thumbnails
    var url = thumbnails.maxres?.url
    if (url.isNullOrEmpty() && thumbnails.high != null) {
        url = thumbnails.high.url
    }
    (view.findViewById(R.id.thumbnail) as ImageView).loadFromUrl(url)
}