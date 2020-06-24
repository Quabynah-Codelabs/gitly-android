package dev.gitly.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.databinding.FragmentUserBinding
import dev.gitly.debugger
import dev.gitly.model.data.User
import dev.gitly.viewmodel.UserViewModel

@AndroidEntryPoint
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding

    private val userViewModel by viewModels<UserViewModel>()
//    private val args by navArgs<>()

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

        val userId: String? = arguments?.getString("userId")
        val user: User? = arguments?.getParcelable("currentUser")
        if (userId.isNullOrEmpty() || user == null) {
            debugger("No user found")
            findNavController().popBackStack()
            return
        }

        userViewModel.getUserById(userId).observe(viewLifecycleOwner, { fetchedUser ->
            binding.run {
                currentUser = fetchedUser
                executePendingBindings()
            }
        })

    }


}