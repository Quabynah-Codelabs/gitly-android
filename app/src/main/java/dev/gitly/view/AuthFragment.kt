package dev.gitly.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.BuildConfig
import dev.gitly.R
import dev.gitly.databinding.AuthFragmentBinding
import dev.gitly.debugger
import dev.gitly.model.DatabaseUtil
import dev.gitly.model.WebServiceUtil
import dev.gitly.viewmodel.AuthViewModel
import dev.gitly.viewmodel.AuthViewModelFactory
import dev.gitly.viewmodel.AuthViewModel_Factory
import okhttp3.OkHttpClient

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels { AuthViewModelFactory(requireContext()) }
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

    override fun onStart() {
        super.onStart()
        login()
    }

    override fun onResume() {
        super.onResume()
        val data = requireActivity().intent?.data
        if (data != null && data.toString().startsWith(BuildConfig.AUTH_CALLBACK)) {
            val code = data.getQueryParameter("code")
            debugger("Code -> $code")
            val authPrefs = DatabaseUtil.provideAuthPrefs(
                requireContext()
            )
            viewModel.getAccessToken(code, authPrefs)
        }
    }

    private fun login() {
        with(Intent(Intent.ACTION_VIEW, Uri.parse(AUTH_URI))) {
            startActivity(this)
        }
    }

    companion object {
        private const val AUTH_URI =
            "https://github.com/login/oauth/authorize?client_id=${BuildConfig.CLIENT_ID}&scope=repo&redirect_uri=${BuildConfig.AUTH_CALLBACK}"
    }
}