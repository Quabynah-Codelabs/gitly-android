package dev.gitly.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.core.prefs.KThemes
import dev.gitly.core.prefs.ThemePrefs
import dev.gitly.databinding.HomeFragmentBinding
import dev.gitly.view.adapter.UsersAdapter
import dev.gitly.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding

    @Inject
    lateinit var themePrefs: ThemePrefs

    @Inject
    lateinit var authPrefs: AuthPrefs

    private val userViewModel by viewModels<UserViewModel>()
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // init adapter
        usersAdapter = UsersAdapter { user ->
            val actionNavDestHomeToNavDestUser =
                HomeFragmentDirections.actionNavDestHomeToNavDestUser(
                    currentUser = user,
                    userId = user?.id
                )
            findNavController().navigate(actionNavDestHomeToNavDestUser)
        }

        // setup binding
        binding.run {
            userId = authPrefs.userId

            moreMenuItem.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionNavDestHomeToNavDestUser(userId = authPrefs.userId)
                )
            }
            searchContainer.setOnClickListener { findNavController().navigate(R.id.action_nav_dest_home_to_nav_dest_search) }
            themeMenuItem.setOnClickListener {
                when (themePrefs.currentTheme) {
                    KThemes.LIGHT -> themePrefs.updateTheme(KThemes.DARK)
                    KThemes.DARK -> themePrefs.updateTheme(KThemes.LIGHT)
                    KThemes.FOLLOW_SYSTEM -> themePrefs.updateTheme(KThemes.DARK)
                }
            }

            homeMentorList.run {
                adapter = usersAdapter
            }
            executePendingBindings()
        }

        // observe current theme
        themePrefs.liveTheme.observe(viewLifecycleOwner, { currentTheme ->
            // update theme icon
            binding.themeMenuItem.run {
                when (currentTheme) {
                    KThemes.LIGHT -> setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_moon
                        )
                    )
                    KThemes.DARK -> setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_sun
                        )
                    )
                    KThemes.FOLLOW_SYSTEM -> setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_twotone_follow_system
                        )
                    )
                    else -> themePrefs.updateTheme(KThemes.LIGHT)
                }
            }
        })

        lifecycleScope.launchWhenStarted {
            // Load all mentors
            userViewModel.getUsersStream().collectLatest { usersPagingData ->
                usersAdapter.submitData(usersPagingData)
            }

            // Load all blog posts
            // TODO: Load all blog posts here
        }

    }

}