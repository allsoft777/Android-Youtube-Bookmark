package com.owllife.youtubebookmark.presentation.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.configureDefaultToolbar
import com.owllife.youtubebookmark.databinding.ActivityEditCategoryBinding
import com.owllife.youtubebookmark.injection.ViewModelFactory
import com.owllife.youtubebookmark.presentation.common.BaseActivity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import kotlinx.android.synthetic.main.toolbar_title_only.*

/**
 * @author owllife.dev
 * @since 20. 6. 3
 */

class EditCategoryActivity : BaseActivity() {

    private lateinit var dataBinding: ActivityEditCategoryBinding
    private var viewModel: EditCategoryViewModel? = null

    companion object {
        fun callingIntent(context: Context) = run {
            val intent = Intent(context, EditCategoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_category)
        dataBinding.lifecycleOwner = this
        dataBinding.viewmodel = getBaseViewModel() as EditCategoryViewModel

        configureListView(viewModel!!, dataBinding.categoryListView)
        configureDefaultToolbar(toolbar, getString(R.string.category_management))
        bindOptionMenuLiveData(this, this)

        viewModel?.let { vm ->
            vm.categoryList.observe(this, Observer {
                vm.dataLoading.value = false
            })
        }
    }

    override fun getBaseViewModel(): BaseViewModel? {
        if (viewModel == null) {
            viewModel = ViewModelProvider(
                this,
                ViewModelFactory(this, application)
            ).get(EditCategoryViewModel::class.java)
        }
        return viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create -> {
                CreateCategoryDialogView().show(this, dataBinding.viewmodel!!)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindOptionMenuLiveData(
        context: Context,
        owner: LifecycleOwner
    ) {
        viewModel?.let { vm ->
            vm.selectedOptionItem.observe(owner, Observer { it ->
                val popup = PopupMenu(context, it.anchor)
                popup.menuInflater.inflate(R.menu.menu_category, popup.menu)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.delete -> vm.deleteCategory()
                        R.id.modify -> {
                            vm.setEditingCategory()
                            EditCategoryDialogView().show(context, vm)
                        }
                    }
                    true
                }
                popup.show()
            })
        }
    }
}