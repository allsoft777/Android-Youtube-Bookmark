package com.owllife.youtubebookmark.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.owllife.youtubebookmark.R
import com.owllife.youtubebookmark.data.logger.Logger
import com.owllife.youtubebookmark.databinding.FragBookmarkListBinding
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import com.owllife.youtubebookmark.injection.ViewModelFactory
import com.owllife.youtubebookmark.presentation.data.SelectedBookmarkData
import com.owllife.youtubebookmark.presentation.player.YoutubePlayerActivity
import com.owllife.youtubebookmark.presentation.util.PresentationConstants

/**
 * @author owllife.dev
 * @since 20. 6. 11
 */
class BookMarkListFragment : Fragment() {

    private lateinit var dataBinding: FragBookmarkListBinding
    private lateinit var listAdapter: BookMarkListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragBookmarkListBinding.inflate(inflater, container, false).apply {
            viewModel = ViewModelProvider(
                requireActivity().viewModelStore,
                ViewModelFactory(requireActivity(), requireActivity().application)
            ).get(BookMarkListViewModel::class.java)
        }
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.lifecycleOwner = this

        val categoryId = getCategoryId()
        dataBinding.categoryId = categoryId

        @Suppress("ReplaceGetOrSet")
        dataBinding.viewModel?.let { vm ->
            vm.getBookmarkListData(categoryId).observe(requireActivity(), Observer {
                vm.setDataLoading(getCategoryId(), false)
            })

            vm.getBookmarkListData(getCategoryId()).observe(this, Observer {
                Logger.d("KSI", "main ------ " + getCategoryId())
            })
        }

        listAdapter = BookMarkListAdapter(object : OnItemClickListener {
            override fun onItemClicked(item: BookMarkEntity) {
                startActivity(YoutubePlayerActivity.callingIntent(activity!!, item))
            }

            override fun onOptionItemClicked(data: SelectedBookmarkData) {
                dataBinding.viewModel?.setSelectedOptionItem(data)
            }
        })

        dataBinding.bookmarkListview.adapter = listAdapter
        bindOptionMenuLiveData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        dataBinding.viewModel!!.fetchDataFromLocalDb(getCategoryId())
    }

    private fun getCategoryId(): Int {
        arguments?.let {
            return it.getInt(PresentationConstants.KEY_CATEGORY_ID, -1)
        }
        return -1
    }

    private fun bindOptionMenuLiveData() {
        dataBinding.viewModel?.let { vm ->
            vm.getSelectedBookmarkData(getCategoryId())
                ?.observe(viewLifecycleOwner, Observer { it ->
                    val popup = PopupMenu(context, it.anchor)
                    popup.menuInflater.inflate(R.menu.menu_bookmark_list, popup.menu)
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.delete -> vm.deleteSelectedBookmark(getCategoryId())
                        }
                        popup.dismiss()
                        true
                    }
                    popup.show()
                })
        }
    }
}