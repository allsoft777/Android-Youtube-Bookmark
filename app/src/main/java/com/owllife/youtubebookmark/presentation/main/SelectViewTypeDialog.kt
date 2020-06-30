package com.owllife.youtubebookmark.presentation.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.entity.EntityConstants
import kotlinx.android.synthetic.main.dialog_view_type.view.*

/**
 * @author owllife.dev
 * @since 20. 6. 29
 */
class SelectViewTypeDialog {

    private var alertDialog: AlertDialog? = null

    @SuppressLint("InflateParams")
    fun show(parentContext: Context, type: Int?, updateTypeFunc: (type: Int) -> Unit) {
        dismiss()
        val builder = AlertDialog.Builder(parentContext, R.style.DialogStyle)
        val view = LayoutInflater.from(parentContext).inflate(R.layout.dialog_view_type, null)

        when (type) {
            EntityConstants.VIEW_TYPE_FULL -> view.radio_group.check(view.full_type.id)
            EntityConstants.VIEW_TYPE_SIMPLE -> view.radio_group.check(view.simple_type.id)
            else -> view.radio_group.check(view.full_type.id)
        }

        builder.setView(view)
        builder.setNegativeButton(R.string.cancel) { _, _ -> }
        builder.setPositiveButton(R.string.done) { _, _ ->
            val typeKey = when (view.radio_group.checkedRadioButtonId) {
                view.full_type.id -> EntityConstants.VIEW_TYPE_FULL
                view.simple_type.id -> EntityConstants.VIEW_TYPE_SIMPLE
                else -> EntityConstants.VIEW_TYPE_FULL
            }
            updateTypeFunc(typeKey)
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun dismiss() {
        alertDialog?.dismiss()
    }
}