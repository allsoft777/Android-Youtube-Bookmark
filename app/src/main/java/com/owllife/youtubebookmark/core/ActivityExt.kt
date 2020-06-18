package com.owllife.youtubebookmark.core

import android.app.Activity
import android.widget.Toast

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */

fun Activity.showToastMsg(msg: String) =
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()