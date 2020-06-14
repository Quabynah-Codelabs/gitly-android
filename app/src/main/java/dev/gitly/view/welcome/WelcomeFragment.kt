package dev.gitly.view.welcome

import `in`.uncod.android.bypass.Bypass
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.core.util.HtmlUtils
import dev.gitly.databinding.WelcomeFragmentBinding

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private lateinit var binding: WelcomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.welcome_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // setup binding
        binding.run {
            val md = Bypass(requireContext(), Bypass.Options())
            val privacyMessage =
                md.markdownToSpannable(getString(R.string.privacy_policy), privacyLink, null)
            HtmlUtils.setTextWithNiceLinks(privacyLink, privacyMessage)
            executePendingBindings()
        }

    }

}
