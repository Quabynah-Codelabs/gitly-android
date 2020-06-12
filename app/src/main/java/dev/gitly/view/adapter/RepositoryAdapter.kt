package dev.gitly.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.gitly.R
import dev.gitly.databinding.ItemRepoBinding
import dev.gitly.model.data.Repo
import javax.inject.Inject

class RepoAdapter @Inject constructor() : ListAdapter<Repo, RepoAdapter.RepoViewHolder>(DIFF_UTIL) {

    class RepoViewHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(repository: Repo) {
            binding.run {
                repo = repository
                executePendingBindings()
            }
        }
    }

    companion object {
        private val DIFF_UTIL: DiffUtil.ItemCallback<Repo> =
            object : DiffUtil.ItemCallback<Repo>() {
                override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                    oldItem.id == newItem.id && oldItem.url == newItem.url

                override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                    oldItem == newItem
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_repo,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}