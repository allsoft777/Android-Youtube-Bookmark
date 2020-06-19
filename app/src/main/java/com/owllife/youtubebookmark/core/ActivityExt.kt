package com.owllife.youtubebookmark.core

import android.app.Activity
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