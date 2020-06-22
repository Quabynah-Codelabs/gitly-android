package dev.gitly.view.home

import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.core.prefs.KThemes
import dev.gitly.core.prefs.ThemePrefs
import dev.gitly.databinding.HomeFragmentBinding
import dev.gitly.debugger
import dev.gitly.viewmodel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels()
    private lateinit var binding: HomeFragmentBinding

    @Inject
    lateinit var themePrefs: ThemePrefs

    @Inject
    lateinit var authPrefs: AuthPrefs

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
            userId = authPrefs.userId
            executePendingBindings()
        }

        // observe current user
        viewModel.currentUser.observe(viewLifecycleOwner, { user ->
            debugger("Signed in as ${user?.name}")
        })

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_item_search)?.run {
            isVisible = !authPrefs.userId.isNullOrEmpty()
        }
        val themeMenuItem = menu.findItem(R.id.menu_item_theme)
        themePrefs.liveTheme.observe(viewLifecycleOwner, { theme ->
            themeMenuItem?.icon = when (theme) {
                KThemes.FOLLOW_SYSTEM -> ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_twotone_follow_system,
                    null
                )
                KThemes.DARK -> ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_twotone_toggle_day, null
                )
                KThemes.LIGHT -> ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_twotone_toggle_night, null
                )
                else -> ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_twotone_toggle_night, null
                )
            }
        })
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_more -> {
                // show bottom sheet or new page
                true
            }
            R.id.menu_item_search -> {
                findNavController().navigate(R.id.action_nav_dest_home_to_nav_dest_search)
                true
            }
            R.id.menu_item_theme -> {
                // toggle theme
                when (themePrefs.currentTheme) {
                    KThemes.LIGHT -> themePrefs.updateTheme(KThemes.DARK)
                    KThemes.DARK -> themePrefs.updateTheme(KThemes.LIGHT)
                    KThemes.FOLLOW_SYSTEM -> themePrefs.updateTheme(KThemes.DARK)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}