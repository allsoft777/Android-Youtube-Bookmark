package com.owllife.youtubebookmark.presentation.profile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.owllife.youtubebookmark.domain.firebase.MyProfileData

/**
 * @author owllife.dev
 * @since 20. 6. 19
 */
@BindingAdapter("profile_thumbnail")
fun renderProfileThumbnail(view: ImageView, data: MyProfileData?) {
    if (data == null) {
        return
    }
    Glide.with(view.context)
        .load(data.photoUrl)
        .apply(RequestOptions().circleCrop())
        .into(view)
}