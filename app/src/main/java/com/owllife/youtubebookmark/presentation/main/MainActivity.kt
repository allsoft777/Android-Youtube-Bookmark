package com.owllife.youtubebookmark.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.core.configureDefaultToolbar
import com.owllife.youtubebookmark.databinding.ActivityMainBinding
import com.owllife.youtubebookmark.injection.ViewModelFactory
import com.owllife.youtubebookmark.presentation.category.EditCategoryActivity
import com.owllife.youtubebookmark.presentation.common.BaseActivity
import com.owllife.youtubebookmark.presentation.common.BaseViewModel
import com.owllife.youtubebookmark.presentation.editbookmark.EditBookMarkActivity
import com.owllife.youtubebookmark.presentation.profile.ProfileActivity
import com.owllife.youtubebookmark.presentation.util.PresentationConstants
import kotlinx.android.synthetic.main.toolbar_title_only.*

class MainActivity : BaseActivity() {

    private lateinit var dataBinding: ActivityMainBinding
    private var viewModel: MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding.lifecycleOwner = this
        dataBinding.viewmodel = getBaseViewModel() as MainViewModel

        viewModel?.let { vm ->
            vm.categoryList.observe(this, Observer {
                vm.dataLoading.value = false
                configureViewPager()
            })
        }

        dataBinding.viewPagerTab.setDefaultTabTextColor(getColor(R.color.primary_text))

        configureDefaultToolbar(toolbar)
    }

    private fun configureViewPager() {
        val pagerAdapter = FragmentPagerItemAdapter(
            supportFragmentManager, getFragmentItems()
        )
        dataBinding.viewpager.apply {
            adapter = pagerAdapter
            offscreenPageLimit = 3
        }
        dataBinding.viewPagerTab.setViewPager(dataBinding.viewpager)
    }

    private fun getFragmentItems(): FragmentPagerItems {
        var creator = FragmentPagerItems.with(this)
        viewModel?.let {
            for (item in it.categoryList.value!!) {
                val bundle = Bundle()
                bundle.apply { putInt(PresentationConstants.KEY_CATEGORY_ID, item.id) }
                creator = creator.add(item.name, BookMarkListFragment::class.java, bundle)
            }
        }
        return creator.create()
    }

    override fun getBaseViewModel(): BaseViewModel? {
        if (viewModel == null) {
            viewModel = ViewModelProvider(
                this,
                ViewModelFactory(this, application)
            ).get(MainViewModel::class.java)
        }
        return viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.category_management -> {
                launchCategoryManagementScreen()
            }
            R.id.bookmark_management -> {
                launchBookmarkManagementScreen()
            }
            R.id.profile -> {
                startActivity(ProfileActivity.callingIntent(this))
            }
        }
        return true
    }

    private fun launchCategoryManagementScreen() {
        val intent = Intent(this, EditCategoryActivity::class.java)
        startActivity(intent)
    }

    private fun launchBookmarkManagementScreen() {
        val intent = Intent(this, EditBookMarkActivity::class.java)
        startActivity(intent)
    }
}
