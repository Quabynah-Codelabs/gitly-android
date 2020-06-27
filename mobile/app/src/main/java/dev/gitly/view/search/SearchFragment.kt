package dev.gitly.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.databinding.SearchFragmentBinding
import dev.gitly.debugger
import dev.gitly.viewmodel.SearchState
import dev.gitly.viewmodel.SearchViewModel

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: SearchFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Observe search result
        viewModel.searchResult.observe(viewLifecycleOwner, { result ->
            debugger("Search result -> $result")
        })

        viewModel.searchState.observe(viewLifecycleOwner, { state ->
            debugger("Search state -> $state")
            when (state) {
                SearchState.NONE -> { // do nothing
                }
                SearchState.COMPLETED -> { // show results
                }
                SearchState.ERROR -> { // show error
                }
                SearchState.STARTED -> { // show progress bar
                }
                else -> {
                }
            }
        })
    }

}