package com.owllife.youtubebookmark.presentation.player

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import com.owllife.youtubebookmark.presentation.util.PresentationConstants

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
class YoutubePlayerViewModel(
    appContext: Context
) : BaseViewModel(appContext) {

    private var _entity: MutableLiveData<BookMarkEntity> = MutableLiveData()
    var entity: LiveData<BookMarkEntity> = _entity

    fun loadData(intent: Intent) {
        val data =
            intent.getParcelableExtra<BookMarkEntity>(PresentationConstants.KEY_BOOKMARK_ENTITY)
        _entity.value = data
    }
}