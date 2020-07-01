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
import com.owllife.youtubebookmark.core.EventObserver
import com.owllife.youtubebookmark.core.showToastMsg
import com.owllife.youtubebookmark.databinding.FragBookmarkListBinding
import com.owllife.youtubebookmark.entity.BookMarkSimpleVO
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
    private val viewModel: BookMarkListViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory(requireActivity().application)
        ).get(BookMarkListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragBookmarkListBinding.inflate(inflater, container, false)
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.lifecycleOwner = this

        val categoryId = getCategoryId()
        dataBinding.categoryId = categoryId

        dataBinding.viewModel?.let { vm ->
            vm.getBookmarkListData(categoryId).observe(viewLifecycleOwner, Observer {
                vm.setDataLoading(getCategoryId(), false)
            })
            vm.toastText.observe(viewLifecycleOwner, Observer {
                activity?.showToastMsg(it)
            })
        }

        bindViewType()
        bindOptionMenuLiveData()
    }

    private fun bindViewType() {
        val sharedMainViewModel: MainViewModel by lazy {
            ViewModelProvider(
                requireActivity(),
                ViewModelFactory(requireActivity().application)
            ).get(MainViewModel::class.java)
        }

        sharedMainViewModel.viewType.observe(viewLifecycleOwner, Observer { viewType ->
            val adapter = dataBinding.bookmarkListview.adapter
            if (adapter == null) {
                initAdapter(viewType)
            } else {
                (adapter as BookMarkListAdapter).setViewType(viewType)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun initAdapter(viewType: Int) {
        listAdapter = BookMarkListAdapter(object : OnItemClickListener {
            override fun onItemClicked(item: BookMarkSimpleVO) {
                startActivity(YoutubePlayerActivity.callingIntent(activity!!, item.id))
            }

            override fun onOptionItemClicked(data: SelectedBookmarkData) {
                dataBinding.viewModel?.setSelectedOptionItem(data)
            }
        }, viewType)
        dataBinding.bookmarkListview.adapter = listAdapter
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
                ?.observe(viewLifecycleOwner, EventObserver { it ->
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