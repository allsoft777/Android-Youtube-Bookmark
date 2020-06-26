package com.owllife.youtubebookmark.presentation.main

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.owllife.youtubebookmark.core.gone
import com.owllife.youtubebookmark.core.visible
import com.owllife.youtubebookmark.entity.CategoryEntireVO

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
@BindingAdapter("main_view_category_items")
fun renderTabView(view: SmartTabLayout, items: List<CategoryEntireVO>?) {
    items?.let { updateVisibility(view, items.isEmpty()) }
}

@BindingAdapter("main_view_category_items")
fun renderViewPagerView(view: ViewPager, items: List<CategoryEntireVO>?) {
    items?.let { updateVisibility(view, items.isEmpty()) }
}

private fun updateVisibility(view: View, empty: Boolean) {
    if (empty) {
        view.gone()
    } else {
        view.visible()
    }
}