package dev.gitly.view.welcome

import `in`.uncod.android.bypass.Bypass
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.BuildConfig
import dev.gitly.R
import dev.gitly.core.billing.GitlyBillingService
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.core.util.HtmlUtils
import dev.gitly.databinding.WelcomeFragmentBinding
import dev.gitly.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private lateinit var binding: WelcomeFragmentBinding
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var authPrefs: AuthPrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.welcome_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val gitlyBillingService = GitlyBillingService(requireActivity())
        // gitlyBillingService.buy()

        // setup binding
        binding.run {
            userId = authPrefs.userId

            val md = Bypass(requireContext(), Bypass.Options())
            val privacyMessage =
                md.markdownToSpannable(
                    "Current version ${BuildConfig.VERSION_NAME} ${getString(R.string.store_link)}",
                    privacyLink,
                    null
                )
            HtmlUtils.setTextWithNiceLinks(privacyLink, privacyMessage)

            getStarted.setOnClickListener {
                findNavController().navigate(
                    if (authPrefs.userId.isNullOrEmpty()) R.id.action_nav_dest_welcome_to_nav_dest_register
                    else
                        R.id.action_nav_dest_welcome_to_nav_dest_home
                )
            }

            guestMode.setOnClickListener {
                authViewModel.logout()
                authPrefs.token = BuildConfig.GUEST_TOKEN
                findNavController().navigate(R.id.action_nav_dest_welcome_to_nav_dest_home)
            }


            executePendingBindings()
        }
    }

}
