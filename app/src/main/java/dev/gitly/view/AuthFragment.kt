package dev.gitly.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.databinding.AuthFragmentBinding
import dev.gitly.model.sources.remote.WebService
import dev.gitly.viewmodel.AuthState
import dev.gitly.viewmodel.AuthViewModel
import dev.gitly.viewmodel.AuthViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel /*by viewModels()*/

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

        // Init viewmodel
        viewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(prefs, service)
        ).get(AuthViewModel::class.java)

        // Init snackbar
        snackbar = Snackbar.make(binding.root, "Message goes here", Snackbar.LENGTH_LONG)

        // observe auth state
        viewModel.authState.observe(viewLifecycleOwner, { state ->
            binding.authState = state
            when (state) {
                AuthState.AUTHENTICATING -> snackbar.run {
                    setText("Authenticating...")
                    show()
                }

                AuthState.AUTHENTICATED -> snackbar.run {
                    setText("Welcome back, ${binding.currentUser?.name}!")
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

        // observe user state
        viewModel.currentUser.observe(viewLifecycleOwner, { user ->
            binding.currentUser = user
        })

        // perform binding
        binding.run {
            prefs = this@AuthFragment.prefs
            viewModel = this@AuthFragment.viewModel
            userEmail.addTextChangedListener { email = it.toString() }
            userPassword.addTextChangedListener { password = it.toString() }
            executePendingBindings()
        }
    }

}