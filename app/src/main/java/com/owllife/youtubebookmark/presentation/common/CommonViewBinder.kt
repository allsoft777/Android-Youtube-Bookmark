package com.owllife.youtubebookmark.presentation.common

import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import com.owllife.youtubebookmark.core.gone
import com.owllife.youtubebookmark.core.visible

/**
 * @author owllife.dev
 * @since 20. 6. 26
 */
@BindingAdapter("common_no_items")
fun renderNoItemsView(textView: TextView, isEmpty: Boolean) {
    if (isEmpty) {
        textView.visible()
    } else {
        textView.gone()
    }
}

@BindingAdapter("common_loading")
fun renderLoadingView(loadingView: ContentLoadingProgressBar, isLoading: Boolean) {
    if (isLoading) {
        loadingView.visible()
    } else {
        loadingView.gone()
    }
}