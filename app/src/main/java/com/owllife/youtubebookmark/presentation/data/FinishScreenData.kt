package com.owllife.youtubebookmark.presentation.data

/**
 * @author owllife.dev
 * @since 20. 6. 17
 */
sealed class FinishScreenData {
    object NoData : FinishScreenData()
    data class WithData(val resultCode: Int) : FinishScreenData()
}