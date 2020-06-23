package com.owllife.youtubebookmark.core

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.BaseRequestOptions

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
fun ImageView.loadFromUrl(url: String?) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .into(this)

fun ImageView.loadFromUrl(url: String?, options: BaseRequestOptions<*>) {
    Glide.with(this.context.applicationContext)
        .load(url)
        .apply(options)
        .into(this)
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}