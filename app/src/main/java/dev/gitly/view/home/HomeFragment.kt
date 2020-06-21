package dev.gitly.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.databinding.HomeFragmentBinding
import dev.gitly.viewmodel.UserViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels()

    private lateinit var binding: HomeFragmentBinding

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
            executePendingBindings()
        }

        // observe current user
        viewModel.currentUser.observe(viewLifecycleOwner, { user ->
            binding.currentUser = user
        })
    }

}