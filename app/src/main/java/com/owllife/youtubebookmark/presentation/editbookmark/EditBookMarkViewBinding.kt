package com.owllife.youtubebookmark.presentation.editbookmark

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.bumptech.glide.Glide
import com.owllife.youtubebookmark.data.logger.Logger
import com.owllife.youtubebookmark.domain.entity.CategoryEntity
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
    var url = thumbnails.default.url
    if (!thumbnails.high.url.isEmpty()) {
        url = thumbnails.high.url
    } else if (!thumbnails.medium.url.isEmpty()) {
        url = thumbnails.medium.url
    }

    Glide.with(view.context)
        .load(url)
        .into(view)
}

@BindingAdapter("selectedValue")
fun bindSpinnerData(
    pAppCompatSpinner: Spinner, categoryList: List<CategoryEntity>
) {
    pAppCompatSpinner.onItemSelectedListener = object : OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            Logger.d("KSI", position.toString())
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}

@InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
fun captureSelectedValue(pAppCompatSpinner: AppCompatSpinner): String? {
    return pAppCompatSpinner.selectedItem as String
}