package com.owllife.youtubebookmark.presentation.category

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.databinding.DialogCreateCategoryBinding
import com.owllife.youtubebookmark.presentation.util.hideKeyboard
import com.owllife.youtubebookmark.presentation.util.showKeyboard

/**
 * @author owllife.dev
 * @since 20. 6. 6
 */
class CreateCategoryDialogView {

    fun show(parentContext: Context, viewModel: EditCategoryViewModel) {
        val builder = AlertDialog.Builder(parentContext, R.style.DialogStyle)
        builder.setCancelable(false)
        builder.setTitle(R.string.msg_input_category_name)

        val dataBinding: DialogCreateCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parentContext),
            R.layout.dialog_create_category, null, false
        )
        dataBinding.viewModel = viewModel
        builder.setNegativeButton(R.string.cancel) { dialog, _ ->
            hideKeyboard(parentContext, dataBinding.inputText.windowToken)
            dialog.dismiss()
        }
        builder.setPositiveButton(R.string.done) { _, _ ->
            hideKeyboard(parentContext, dataBinding.inputText.windowToken)
            viewModel.insertNewCategory()
        }

        val alertDialog = builder.create()
        alertDialog.setView(dataBinding.root)
        alertDialog.setCancelable(false)
        alertDialog.setOnShowListener {
            dataBinding.inputText.requestFocus()
            val pos: Int = dataBinding.inputText.text.length
            dataBinding.inputText.setSelection(pos)
            showKeyboard(parentContext)
        }
        alertDialog.show()
    }
}