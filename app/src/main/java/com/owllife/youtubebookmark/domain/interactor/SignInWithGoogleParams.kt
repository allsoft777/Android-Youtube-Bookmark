package com.owllife.youtubebookmark.domain.interactor

import android.app.Activity
import android.content.Intent

/**
 * @author owllife.dev
 * @since 20. 6. 19
 */
data class SignInWithGoogleParams(
    var intent: Intent?,
    var activity: Activity?
)