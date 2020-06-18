package com.owllife.youtubebookmark.core

import android.view.View
import android.widget.Toast

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */
fun View.showToastMsg(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()