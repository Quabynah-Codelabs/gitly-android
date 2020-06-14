package dev.gitly.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.databinding.AuthFragmentBinding
import dev.gitly.debugger
import dev.gitly.model.sources.remote.WebService
import dev.gitly.viewmodel.AuthState
import dev.gitly.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var prefs: AuthPrefs

    @Inject
    lateinit var service: WebService

    private lateinit var binding: AuthFragmentBinding
    private lateinit var snackbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Init snackbar
        snackbar = Snackbar.make(binding.root, "Message goes here", Snackbar.LENGTH_LONG)

        // observe token change
        prefs.refreshedToken.observe(viewLifecycleOwner, { refreshedToken ->
            debugger("Refreshed token -> $refreshedToken")
            binding.token = refreshedToken
        })

        // observe auth state
        viewModel.authState.observe(viewLifecycleOwner, { state ->
            binding.authState = state
            when (state) {
                AuthState.AUTHENTICATING -> snackbar.run {
                    setText("Authenticating...")
                    show()
                }

                AuthState.AUTHENTICATED -> snackbar.run {
                    setText("Welcome back!")
                    show()
                }

                AuthState.ERROR -> snackbar.run {
                    setText("Invalid email or password")
                    show()
                }

                AuthState.NONE -> {
                }

                else -> {
                }
            }
        })

        // perform binding
        binding.run {
            viewModel = this@AuthFragment.viewModel
            userEmail.addTextChangedListener { email = it.toString() }
            userPassword.addTextChangedListener { password = it.toString() }
            navHome.setOnClickListener { findNavController().navigate(R.id.action_nav_dest_auth_to_nav_dest_home) }
            executePendingBindings()
        }
    }

}