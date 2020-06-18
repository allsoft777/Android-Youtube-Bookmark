package com.owllife.youtubebookmark.presentation.util

import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
fun hideKeyboard(appContext: Context, windowToken: IBinder?) {
    val inputMethodManager =
        appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun showKeyboard(appContext: Context) {
    val inputMethodManager =
        appContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}