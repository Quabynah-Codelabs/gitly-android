package dev.gitly.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.databinding.FragmentUserBinding
import dev.gitly.debugPrint
import dev.gitly.debugger
import dev.gitly.viewmodel.UserViewModel

@AndroidEntryPoint
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding

    private val userViewModel by viewModels<UserViewModel>()
    private val args by navArgs<UserFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments.debugPrint()

        if (args.userId.isNullOrEmpty()) {
            debugger("No user found")
            findNavController().popBackStack()
            return
        }

        // observe user
        userViewModel.getUserById(args.userId!!).observe(viewLifecycleOwner, { fetchedUser ->
            binding.run {
                currentUser = fetchedUser
                executePendingBindings()
            }
        })

    }


}