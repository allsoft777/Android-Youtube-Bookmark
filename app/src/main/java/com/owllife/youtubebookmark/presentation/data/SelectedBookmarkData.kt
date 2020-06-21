package com.owllife.youtubebookmark.presentation.data

import android.view.View
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity

/**
 * @author owllife.dev
 * @since 20. 6. 22
 */
data class SelectedBookmarkData(val anchor: View, val item: BookMarkEntity)