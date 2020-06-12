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
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.BuildConfig
import dev.gitly.R
import dev.gitly.databinding.AuthFragmentBinding

@AndroidEntryPoint
class AuthFragment : Fragment() {

    //    @Inject
//    lateinit var authPrefs: AuthPrefs
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

        binding.run {
        }
        login()
    }

    override fun onResume() {
        super.onResume()
        val data = requireActivity().intent?.data
        if (data != null && data.toString().startsWith(BuildConfig.AUTH_CALLBACK))
            Toast.makeText(requireContext(), "Yay!!!", Toast.LENGTH_SHORT).show()
        val code = data?.getQueryParameter("code")

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