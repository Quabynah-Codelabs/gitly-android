package dev.gitly.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.databinding.AuthFragmentBinding
import dev.gitly.debugger
import dev.gitly.viewmodel.AuthState
import dev.gitly.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {


    @Inject
    lateinit var prefs: AuthPrefs
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var snackbar: Snackbar
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

        // Init snackbar
        snackbar = Snackbar.make(binding.root, "Message goes here", Snackbar.LENGTH_LONG)

        // observe token change
        prefs.refreshedUserId.observe(viewLifecycleOwner, { refreshedUserId ->
            debugger("Refreshed user id -> $refreshedUserId")
            binding.token = refreshedUserId
            if (!refreshedUserId.isNullOrEmpty())
                findNavController().navigate(R.id.action_nav_dest_auth_to_nav_dest_home)
        })

        // observe auth state
        viewModel.authState.observe(viewLifecycleOwner, { state ->
            binding.authState = state
            when (state) {
                AuthState.AUTHENTICATING -> snackbar.run {
                    setText("Authenticating...")
                    show()
                }

                AuthState.AUTHENTICATED -> { // do nothing
                    lifecycleScope.launch {
                        snackbar.run {
                            setText("Fetching user data...")
                            show()
                        }
                    }
                }

                AuthState.ERROR -> snackbar.run {
                    setText("Invalid email or password")
                    show()
                }

                AuthState.NONE -> { // do nothing
                }

                else -> { // do nothing
                }
            }
        })

        // perform binding
        binding.run {
            viewModel = this@AuthFragment.viewModel
            userEmail.addTextChangedListener { email = it.toString() }
            userPassword.addTextChangedListener { password = it.toString() }
            executePendingBindings()
        }
    }

}