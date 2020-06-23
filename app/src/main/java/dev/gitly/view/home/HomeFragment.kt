package dev.gitly.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.core.prefs.KThemes
import dev.gitly.core.prefs.ThemePrefs
import dev.gitly.databinding.HeaderHomeBinding
import dev.gitly.databinding.HomeFragmentBinding
import dev.gitly.debugPrint
import dev.gitly.debugger
import dev.gitly.viewmodel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels()
    private lateinit var binding: HomeFragmentBinding
    private lateinit var headerBinding: HeaderHomeBinding

    @Inject
    lateinit var themePrefs: ThemePrefs

    @Inject
    lateinit var authPrefs: AuthPrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        headerBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.header_home,
            binding.drawer,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // setup binding
        binding.run {
            model = viewModel
            userId = authPrefs.userId

            moreMenuItem.setOnClickListener { findNavController().navigate(R.id.action_nav_dest_home_to_nav_dest_search) }
            searchMenuItem.setOnClickListener { findNavController().navigate(R.id.action_nav_dest_home_to_nav_dest_search) }
            themeMenuItem.setOnClickListener {
                when (themePrefs.currentTheme) {
                    KThemes.LIGHT -> themePrefs.updateTheme(KThemes.DARK)
                    KThemes.DARK -> themePrefs.updateTheme(KThemes.LIGHT)
                    KThemes.FOLLOW_SYSTEM -> themePrefs.updateTheme(KThemes.DARK)
                }
            }
            executePendingBindings()
        }


        // observe current user
        viewModel.currentUser.observe(viewLifecycleOwner, { user ->
            debugger("Signed in as ${user?.name}")

            // setup header
            headerBinding.run {
                currentUser = user
                currentUser.debugPrint()
                executePendingBindings()
            }
        })

        // observe current theme
        themePrefs.liveTheme.observe(viewLifecycleOwner, { currentTheme ->
            // update theme icon
            binding.themeMenuItem.run {
                when (currentTheme) {
                    KThemes.LIGHT -> setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_twotone_toggle_night
                        )
                    )
                    KThemes.DARK -> setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_twotone_toggle_day
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

    }

}