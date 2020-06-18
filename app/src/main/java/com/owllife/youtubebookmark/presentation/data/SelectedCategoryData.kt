package com.owllife.youtubebookmark.presentation.data

import android.view.View
import com.owllife.youtubebookmark.domain.entity.CategoryEntity

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
data class SelectedCategoryData(val anchor: View, val item: CategoryEntity)