package com.owllife.youtubebookmark.core

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.owllife.youtubebookmark.R
import kotlinx.android.synthetic.main.toolbar_title_only.view.*

/**
 * @author owllife.dev
 * @since 20. 6. 10
 */

fun Activity.showToastMsg(msg: String) =
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

fun AppCompatActivity.configureDefaultToolbar(
    toolbar: Toolbar,
    title: String? = getString(R.string.app_name)
) = run {
    setSupportActionBar(toolbar)
    supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    supportActionBar!!.setDisplayShowTitleEnabled(false)
    toolbar.center_title.text = title
}

fun Activity.navToLauncherTask() {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appTasks = activityManager.appTasks
    for (task in appTasks) {
        val baseIntent = task.taskInfo.baseIntent
        val categories = baseIntent.categories
        if (categories != null && categories.contains(Intent.CATEGORY_LAUNCHER)) {
            task.moveToFront()
            return
        }
    }
}