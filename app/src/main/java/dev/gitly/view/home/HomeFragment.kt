package dev.gitly.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.core.prefs.ThemePrefs
import dev.gitly.databinding.HomeFragmentBinding
import dev.gitly.view.adapter.UserLoadStateAdapter
import dev.gitly.view.adapter.UsersAdapter
import dev.gitly.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels()
    private val usersAdapter = UsersAdapter()
    private lateinit var binding: HomeFragmentBinding

    @Inject
    lateinit var themePrefs: ThemePrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // setup binding
        binding.run {
            model = viewModel
            usersList.run {
                usersAdapter.withLoadStateHeaderAndFooter(
                    header = UserLoadStateAdapter { },
                    footer = UserLoadStateAdapter { }
                )
                adapter = usersAdapter
                setHasFixedSize(false)
            }
            executePendingBindings()
        }

        // observe current user
        viewModel.currentUser.observe(viewLifecycleOwner, { user ->
            binding.currentUser = user
        })

        lifecycleScope.launch {
            viewModel.getUsersStream().collectLatest {
                usersAdapter.submitData(it)
            }
        }

    }

}