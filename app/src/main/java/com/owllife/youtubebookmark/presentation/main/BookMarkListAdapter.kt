package com.owllife.youtubebookmark.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.owllife.youtubebookmark.databinding.ListItemBookmarkBinding
import com.owllife.youtubebookmark.domain.entity.BookMarkEntity

/**
 * @author owllife.dev
 * @since 20. 6. 15
 */
class BookMarkListAdapter(private var viewModel: BookMarkListViewModel) :
    ListAdapter<BookMarkEntity, BookMarkListAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, viewModel)
    }

    class ViewHolder private constructor(private val binding: ListItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookMarkEntity, viewModel: BookMarkListViewModel) {
            binding.bookmark = item
            binding.viewmodel = viewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val li = LayoutInflater.from(parent.context)
                val binding = ListItemBookmarkBinding.inflate(li, parent, false)
                return ViewHolder(binding)
            }
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
