package com.owllife.youtubebookmark.presentation.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.owllife.youtubebookmark.databinding.ListItemCategoryBinding
import com.owllife.youtubebookmark.entity.CategoryEntireVO

/**
 * @author owllife.dev
 * @since 20. 6. 5
 */
class CategoryAdapter(private val viewModel: EditCategoryViewModel) :
    ListAdapter<CategoryEntireVO, CategoryAdapter.ViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    class ViewHolder private constructor(private val binding: ListItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: EditCategoryViewModel, item: CategoryEntireVO) {
            binding.viewModel = viewModel
            binding.category = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val li = LayoutInflater.from(parent.context)
                val binding = ListItemCategoryBinding.inflate(li, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<CategoryEntireVO>() {
    override fun areItemsTheSame(oldItem: CategoryEntireVO, newItem: CategoryEntireVO): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryEntireVO, newItem: CategoryEntireVO): Boolean {
        return oldItem == newItem
    }
}
