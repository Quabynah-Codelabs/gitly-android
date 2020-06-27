package dev.gitly.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.gitly.R
import dev.gitly.databinding.ItemExpertiseBinding
import dev.gitly.model.data.Category
import dev.gitly.viewmodel.CategoryViewModel

class CategoryListAdapter constructor(private val categoryViewModel: CategoryViewModel) :
    ListAdapter<Category, CategoryListAdapter.CategoryListViewHolder>(CATEGORY_DIFF) {

    companion object {
        private val CATEGORY_DIFF: DiffUtil.ItemCallback<Category> =
            object : DiffUtil.ItemCallback<Category>() {
                override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean =
                    oldItem == newItem
            }
    }


    class CategoryListViewHolder(
        private val binding: ItemExpertiseBinding,
        private val categoryViewModel: CategoryViewModel
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Category) {
            binding.run {
                category = item
                viewModel = categoryViewModel

                if (category != null && category!!.isEnabled) {
                    cardContainer.run {
                        val defaultCardForegroundColor = cardForegroundColor
                        isChecked = !isChecked
                        if (isChecked) {
                            setCardBackgroundColor(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.secondary
                                )
                            )
                            categoryName.setTextColor(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    R.color.white
                                )
                            )

                        } else setCardBackgroundColor(defaultCardForegroundColor)
                    }
                }
                executePendingBindings()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryListViewHolder {
        val binding = DataBindingUtil.bind<ItemExpertiseBinding>(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_expertise,
                parent,
                false
            )
        )
        return CategoryListViewHolder(binding!!, categoryViewModel)
    }

    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}