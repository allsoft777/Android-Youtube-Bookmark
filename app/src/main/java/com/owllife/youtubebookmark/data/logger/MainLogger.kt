package com.owllife.youtubebookmark.data.logger

import android.util.Log
import com.owllife.youtubebookmark.BuildConfig

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
@Suppress("unused")
object MainLogger {

    private const val TAG = "YOUTUBE_BOOKMARK"

    fun i(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, getPrefix(tag) + msg)
        }
    }

    fun v(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.v(TAG, getPrefix(tag) + msg)
        }
    }

    fun d(tag: String, msg: String) {
        Log.d(TAG, getPrefix(tag) + msg)
    }

    fun e(tag: String, msg: String) {
        Log.e(TAG, getPrefix(tag) + msg)
    }

    private fun getPrefix(prefix: String): String {
        return "[$prefix] "
    }
}