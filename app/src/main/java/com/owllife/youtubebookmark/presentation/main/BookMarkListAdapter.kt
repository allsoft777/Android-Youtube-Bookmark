package com.owllife.youtubebookmark.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.owllife.youtubebookmark.databinding.ListItemBookmarkBinding
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity
import com.owllife.youtubebookmark.presentation.data.SelectedBookmarkData

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
class BookMarkListAdapter(private var onClickListener: OnItemClickListener) :
    ListAdapter<BookMarkEntity, BookMarkListAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = LayoutInflater.from(parent.context)
        val binding = ListItemBookmarkBinding.inflate(li, parent, false)
        return ViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    class ViewHolder constructor(
        private val binding: ListItemBookmarkBinding,
        private val onClickListener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookMarkEntity, pos: Int) {
            binding.bookmark = item
            binding.bodyContainer.setOnClickListener { onClickListener.onItemClicked(item) }

            val optionMenu = SelectedBookmarkData(binding.icMore, item)
            binding.icMore.setOnClickListener { onClickListener.onOptionItemClicked(optionMenu) }
            binding.executePendingBindings()
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<BookMarkEntity>() {
    override fun areItemsTheSame(oldItem: BookMarkEntity, newItem: BookMarkEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookMarkEntity, newItem: BookMarkEntity): Boolean {
        return oldItem == newItem
    }
}

interface OnItemClickListener {
    fun onItemClicked(item: BookMarkEntity)
    fun onOptionItemClicked(data: SelectedBookmarkData)
}
