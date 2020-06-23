package com.owllife.youtubebookmark.core

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
fun ImageView.loadFromUrl(url: String?) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .into(this)