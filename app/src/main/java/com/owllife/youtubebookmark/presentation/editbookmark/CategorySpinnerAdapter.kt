package com.owllife.youtubebookmark.presentation.editbookmark

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.owllife.youtubebookmark.databinding.SpinnerGetviewItemCategoryBinding
import com.owllife.youtubebookmark.domain.entity.CategoryEntity

/**
 * @author owllife.dev
 * @since 20. 6. 12
 */
class CategorySpinnerAdapter(
    context: Context,
    items: List<CategoryEntity>
) : ArrayAdapter<CategoryEntity>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder = DropDownViewHolder.from(parent)
        val item = getItem(position)
        holder.bind(item)
        return holder.itemView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder = DropDownViewHolder.from(parent)
        val item = getItem(position)
        holder.bind(item)
        return holder.itemView
    }

    class DropDownViewHolder private constructor(private val binding: SpinnerGetviewItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CategoryEntity?) {
            binding.category = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): DropDownViewHolder {
                val li = LayoutInflater.from(parent.context)
                val binding = SpinnerGetviewItemCategoryBinding.inflate(li, parent, false)
                return DropDownViewHolder(binding)
            }
        }
    }
}