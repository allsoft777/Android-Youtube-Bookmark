package com.owllife.youtubebookmark.presentation.editbookmark

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.configureDefaultToolbar
import com.owllife.youtubebookmark.databinding.ActivityEditBookmarkBinding
import com.owllife.youtubebookmark.injection.ViewModelFactory
import com.owllife.youtubebookmark.presentation.category.EditCategoryActivity
import com.owllife.youtubebookmark.presentation.common.BaseActivity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import com.owllife.youtubebookmark.presentation.util.hideKeyboard
import kotlinx.android.synthetic.main.toolbar_title_only.*

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
class EditBookMarkActivity : BaseActivity() {

    private lateinit var dataBinding: ActivityEditBookmarkBinding
    private var viewModel: EditBookMarkViewModel? = null

    companion object {
        fun callingIntent(context: Context) = run {
            val intent = Intent(context, EditBookMarkActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_bookmark)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = getBaseViewModel() as EditBookMarkViewModel

        viewModel?.let {
            it.categoryList.observe(this, Observer {
                dataBinding.categorySelector.adapter = CategorySpinnerAdapter(viewModel!!, this, it)
            })
            it.hideInputMethod.observe(this, Observer { method ->
                if (method) {
                    hideKeyboard(this, dataBinding.inputUrlEt.windowToken)
                }
            })
            it.savedToLocalDb.observe(this, Observer { saved ->
                if (saved) {
                    finish()
                }
            })
            it.categoryManagement.observe(this, Observer {
                startActivity(EditCategoryActivity.callingIntent(this))
            })
        }
        configureDefaultToolbar(toolbar, getString(R.string.bookmark_management))
    }

    override fun getBaseViewModel(): BaseViewModel? {
        if (viewModel == null) {
            viewModel = ViewModelProvider(
                this,
                ViewModelFactory(this, application)
            ).get(EditBookMarkViewModel::class.java)
        }
        return viewModel
    }
}