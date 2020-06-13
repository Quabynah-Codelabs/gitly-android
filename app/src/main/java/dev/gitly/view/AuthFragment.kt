package dev.gitly.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.databinding.AuthFragmentBinding

@AndroidEntryPoint
class AuthFragment : Fragment() {

    //    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: AuthFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private fun login() {

    }

}