package com.owllife.youtubebookmark.presentation.main

import android.content.Intent
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
import com.owllife.youtubebookmark.databinding.FragBookmarkListBinding
import com.owllife.youtubebookmark.injection.ViewModelFactory
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
            viewmodel = ViewModelProvider(
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

        loadData()

        @Suppress("ReplaceGetOrSet")
        dataBinding.viewmodel?.let { vm ->
            if (vm.bookmarkList.isEmpty() || vm.bookmarkList.get(categoryId) == null) {
                return
            }
            vm.bookmarkList.get(categoryId)!!.observe(requireActivity(), Observer {
                vm.dataLoading.value = false
            })
            vm.openBookmarkEvent.observe(requireActivity(), EventObserver { entity ->
                val intent = Intent(activity, YoutubePlayerActivity::class.java)
                intent.putExtra(PresentationConstants.KEY_BOOKMARK_ENTITY, entity)
                startActivity(intent)
            })
        }
        listAdapter = BookMarkListAdapter(dataBinding.viewmodel!!)
        dataBinding.bookmarkListview.adapter = listAdapter
        bindOptionMenuLiveData()
    }

    private fun loadData() {
        dataBinding.viewmodel!!.fetchDataFromLocalDb(getCategoryId())
    }

    private fun getCategoryId(): Int {
        arguments?.let {
            return it.getInt(PresentationConstants.KEY_CATEGORY_ID, -1)
        }
        return -1
    }

    private fun bindOptionMenuLiveData() {
        dataBinding.viewmodel?.let { vm ->
            vm.getSelectedBookmarkData()?.observe(viewLifecycleOwner, Observer { it ->
                val popup = PopupMenu(context, it.anchor)
                popup.menuInflater.inflate(R.menu.menu_bookmark_list, popup.menu)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.delete -> vm.deleteSelectedBookmark()

                    }
                    true
                }
                popup.show()
            })
        }
    }
}