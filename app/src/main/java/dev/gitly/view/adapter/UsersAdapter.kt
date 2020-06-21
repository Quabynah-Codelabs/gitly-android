package dev.gitly.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.gitly.R
import dev.gitly.databinding.LoadingStateFooterBinding
import dev.gitly.databinding.UserItemBinding
import dev.gitly.model.data.User

/**
 * Adapter for loading state
 */
class UserLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<UsersAdapter.UsersLoadingStateViewHolder>() {
    override fun onBindViewHolder(
        holder: UsersAdapter.UsersLoadingStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): UsersAdapter.UsersLoadingStateViewHolder =
        UsersAdapter.UsersLoadingStateViewHolder.create(parent, retry)

}

/**
 * User Adapter
 */
class UsersAdapter : PagingDataAdapter<User, UsersAdapter.UserViewHolder>(USER_COMPARATOR) {
    companion object {
        private val USER_COMPARATOR: DiffUtil.ItemCallback<User> =
            object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                    oldItem.id == newItem.id
            }
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder =
        UserViewHolder.create(parent)

    class UsersLoadingStateViewHolder(
        private val binding: LoadingStateFooterBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            binding.run {
                if (loadState is LoadState.Error) {
                    errorMessage.text = loadState.error.localizedMessage
                }
                loading.isVisible = loadState is LoadState.Loading
                retry.isVisible = loadState !is LoadState.Loading
                errorMessage.isVisible = loadState !is LoadState.Loading
            }
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): UsersLoadingStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_state_footer, parent, false)
                val binding = LoadingStateFooterBinding.bind(view)
                return UsersLoadingStateViewHolder(binding, retry)
            }
        }

    }

    class UserViewHolder(
        private val binding: UserItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentUser: User?) {
            binding.run {
                user = currentUser
                executePendingBindings()
            }
        }

        companion object {
            fun create(parent: ViewGroup): UserViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.user_item, parent, false)
                val binding = UserItemBinding.bind(view)
                return UserViewHolder(binding)
            }
        }
    }
}