package com.owllife.youtubebookmark.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.owllife.youtubebookmark.databinding.ListItemBookmarkFullTypeBinding
import com.owllife.youtubebookmark.databinding.ListItemBookmarkSimpleTypeBinding
import com.owllife.youtubebookmark.entity.BookMarkSimpleVO
import com.owllife.youtubebookmark.entity.EntityConstants
import com.owllife.youtubebookmark.presentation.data.SelectedBookmarkData

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
class BookMarkListAdapter(
    private var onClickListener: OnItemClickListener,
    private var viewType: Int
) :
    ListAdapter<BookMarkSimpleVO, RecyclerView.ViewHolder>(TaskDiffCallback()) {

    fun setViewType(type: Int) {
        viewType = type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val li = LayoutInflater.from(parent.context)
        if (viewType == EntityConstants.VIEW_TYPE_FULL) {
            val binding = ListItemBookmarkFullTypeBinding.inflate(li, parent, false)
            return ViewHolderFullType(binding, onClickListener)
        } else if (viewType == EntityConstants.VIEW_TYPE_SIMPLE) {
            val binding = ListItemBookmarkSimpleTypeBinding.inflate(li, parent, false)
            return ViewHolderSimpleType(binding, onClickListener)
        }
        throw IllegalArgumentException("view type is invalid : $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderFullType) {
            val item = getItem(position)
            holder.bind(item)
        } else if (holder is ViewHolderSimpleType) {
            val item = getItem(position)
            holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    class ViewHolderFullType constructor(
        private val binding: ListItemBookmarkFullTypeBinding,
        private val onClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookMarkSimpleVO) {
            binding.bookmark = item
            binding.bodyContainer.setOnClickListener { onClickListener.onItemClicked(item) }

            val optionMenu = SelectedBookmarkData(binding.icMore, item)
            binding.icMore.setOnClickListener { onClickListener.onOptionItemClicked(optionMenu) }
            binding.executePendingBindings()
        }
    }

    class ViewHolderSimpleType constructor(
        private val binding: ListItemBookmarkSimpleTypeBinding,
        private val onClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookMarkSimpleVO) {
            binding.bookmark = item
            binding.bodyContainer.setOnClickListener { onClickListener.onItemClicked(item) }

            val optionMenu = SelectedBookmarkData(binding.icMore, item)
            binding.icMore.setOnClickListener { onClickListener.onOptionItemClicked(optionMenu) }
            binding.executePendingBindings()
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<BookMarkSimpleVO>() {
    override fun areItemsTheSame(oldItem: BookMarkSimpleVO, newItem: BookMarkSimpleVO): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookMarkSimpleVO, newItem: BookMarkSimpleVO): Boolean {
        return oldItem == newItem
    }
}

interface OnItemClickListener {
    fun onItemClicked(item: BookMarkSimpleVO)
    fun onOptionItemClicked(data: SelectedBookmarkData)
}
