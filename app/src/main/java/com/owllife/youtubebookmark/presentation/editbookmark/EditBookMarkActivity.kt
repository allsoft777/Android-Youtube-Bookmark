package com.owllife.youtubebookmark.presentation.editbookmark

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.configureDefaultToolbar
import com.owllife.youtubebookmark.core.showToastMsg
import com.owllife.youtubebookmark.databinding.ActivityEditBookmarkBinding
import com.owllife.youtubebookmark.injection.ViewModelFactory
import com.owllife.youtubebookmark.presentation.category.EditCategoryActivity
import com.owllife.youtubebookmark.presentation.common.BaseActivity
import com.owllife.youtubebookmark.presentation.util.PresentationConstants
import com.owllife.youtubebookmark.presentation.util.hideKeyboard
import kotlinx.android.synthetic.main.toolbar_title_only.*

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
class EditBookMarkActivity : BaseActivity() {

    private lateinit var dataBinding: ActivityEditBookmarkBinding
    private val viewModel: EditBookMarkViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory(application)
        ).get(EditBookMarkViewModel::class.java)
    }

    companion object {
        fun callingIntent(parentContext: Context, categoryId: Int?) = run {
            Intent(parentContext, EditBookMarkActivity::class.java).apply {
                val args = Bundle().apply {
                    putInt(
                        PresentationConstants.KEY_CATEGORY_ID,
                        categoryId ?: EditBookMarkViewModel.INVALID_ID
                    )
                }
                putExtra(PresentationConstants.KEY_ARGS, args)
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_bookmark)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel

        viewModel.let { vm ->
            vm.dataLoading.observe(this, Observer { isLoading ->
                if (isLoading) loadingDialog.value.show() else loadingDialog.value.dismiss()
            })
            vm.categoryList.observe(this, Observer { categoryList ->
                dataBinding.categorySelector.adapter = CategorySpinnerAdapter(this, categoryList)
                viewModel.selectForwardedCategory()
            })
            vm.hideInputMethod.observe(this, Observer { method ->
                if (method) hideKeyboard(this, dataBinding.inputUrlEt.windowToken)
            })
            vm.savedToLocalDb.observe(this, Observer { saved ->
                if (saved) finish()
            })
            vm.categoryManagement.observe(this, Observer {
                startActivity(EditCategoryActivity.callingIntent(this))
            })
            vm.toastText.observe(this, Observer { msg -> showToastMsg(msg) })
        }
        configureDefaultToolbar(toolbar, getString(R.string.bookmark_management))
        viewModel.loadArgs(intent.getBundleExtra(PresentationConstants.KEY_ARGS))
    }
}