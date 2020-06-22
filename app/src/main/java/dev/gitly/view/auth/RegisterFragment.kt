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
import dev.gitly.R
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.databinding.FragmentRegisterBinding
import dev.gitly.debugger
import dev.gitly.viewmodel.AuthState
import dev.gitly.viewmodel.AuthViewModel
import javax.inject.Inject


/**
 * Sign up a new [User]
 */
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    @Inject
    lateinit var prefs: AuthPrefs
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var snackbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
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
            if (!refreshedToken.isNullOrEmpty())
                findNavController().navigate(R.id.action_nav_dest_register_to_nav_dest_account_setup)
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
            viewModel = this@RegisterFragment.viewModel
            userName.addTextChangedListener { name = it.toString() }
            userEmail.addTextChangedListener { email = it.toString() }
            userPassword.addTextChangedListener { password = it.toString() }
            executePendingBindings()
        }
    }
}