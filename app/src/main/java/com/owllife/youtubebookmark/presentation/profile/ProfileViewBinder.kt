package com.owllife.youtubebookmark.presentation.profile

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.gone
import com.owllife.youtubebookmark.core.loadFromUrl
import com.owllife.youtubebookmark.core.visible
import com.owllife.youtubebookmark.domain.firebase.MyProfileData

/**
 * @author owllife.dev
 * @since 20. 6. 19
 */
@BindingAdapter("profile_data")
fun renderProfileData(
    view: LinearLayout,
    data: MyProfileData?
) {
    if (data == null) {
        view.gone()
        return
    }
    view.visible()
    (view.findViewById(R.id.profile_img) as ImageView).loadFromUrl(data.photoUrl)
    (view.findViewById(R.id.email) as TextView).text = data.email
    (view.findViewById(R.id.display_name) as TextView).text = data.displayName
}
