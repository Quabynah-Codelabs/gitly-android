package dev.gitly.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.databinding.FragmentUserBinding
import dev.gitly.debugger
import dev.gitly.view.user.tabs.MentorBlogPostsFragment
import dev.gitly.view.user.tabs.MentorExperienceFragment
import dev.gitly.view.user.tabs.MentorReviewFragment
import dev.gitly.view.user.tabs.PersonalInfoFragment
import dev.gitly.view.widget.transformers.DepthTransformation
import dev.gitly.viewmodel.UserViewModel


@AndroidEntryPoint
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding

    private val userViewModel by viewModels<UserViewModel>()
    private val args by navArgs<UserFragmentArgs>()
    private val pageTitles = mutableListOf("Personal Info", "Experiences", "Reviews", "Blog posts")

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

        when {
            // get current user passed through arguments
            args.currentUser != null -> binding.currentUser = args.currentUser

            // get user id passed through arguments
            !args.userId.isNullOrEmpty() -> {
                // observe user
                userViewModel.getUserById(args.userId!!)
                    .observe(viewLifecycleOwner, { fetchedUser ->
                        binding.run {
                            currentUser = fetchedUser
                            executePendingBindings()
                        }
                    })
            }

            // if none of the above is provided, leave page
            else -> {
                debugger("No user found")
                Toast.makeText(requireContext(), "No user found", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                return
            }
        }

        binding.run {
            userPager.adapter = ViewPageFragmentAdapter(requireActivity())
            TabLayoutMediator(tabs, userPager) { tab, position ->
                tab.text = pageTitles[position]
            }.attach()
            userPager.setPageTransformer(DepthTransformation())
            executePendingBindings()
        }

    }

    /**
     * Adapter for [ViewPager2]
     */
    internal class ViewPageFragmentAdapter constructor(context: FragmentActivity) :
        FragmentStateAdapter(context) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> PersonalInfoFragment()
                1 -> MentorExperienceFragment()
                2 -> MentorReviewFragment()
                else -> MentorBlogPostsFragment()
            }
        }

    }


}