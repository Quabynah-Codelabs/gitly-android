package dev.gitly.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dev.gitly.BuildConfig
import dev.gitly.R
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.databinding.AuthFragmentBinding
import dev.gitly.debugger
import javax.inject.Inject

class AuthFragment : Fragment() {

    @Inject
    lateinit var authPrefs: AuthPrefs
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

        debugger("User id -> ${authPrefs.userId}")
        binding.run {

        }
    }

    private fun login() {
        with(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/login/oauth/authorize?client_id=${BuildConfig.CLIENT_ID}")
            )
        ) {

        }
    }
}