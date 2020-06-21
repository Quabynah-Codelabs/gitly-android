package dev.gitly.view.welcome

import `in`.uncod.android.bypass.Bypass
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.BuildConfig
import dev.gitly.R
import dev.gitly.core.util.HtmlUtils
import dev.gitly.databinding.WelcomeFragmentBinding
import dev.gitly.debugger
import dev.gitly.model.data.User
import dev.gitly.model.sources.local.daos.UserDao
import dev.gitly.view.adapter.UserLoadStateAdapter
import dev.gitly.view.adapter.UsersAdapter
import dev.gitly.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private lateinit var binding: WelcomeFragmentBinding

    @Inject
    lateinit var userDao: UserDao

    private val usersAdapter = UsersAdapter()
    private val userViewModel by viewModels<UserViewModel>()

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
                md.markdownToSpannable(
                    "Current version ${BuildConfig.VERSION_NAME} ${getString(R.string.store_link)}",
                    privacyLink,
                    null
                )
            HtmlUtils.setTextWithNiceLinks(privacyLink, privacyMessage)

            getStarted.setOnClickListener { findNavController().navigate(R.id.action_nav_dest_welcome_to_nav_dest_auth) }

            // TODO: Cut this section to the home screen
            usersList.run {
                usersAdapter.withLoadStateHeaderAndFooter(
                    header = UserLoadStateAdapter { },
                    footer = UserLoadStateAdapter { }
                )
                adapter = usersAdapter
                setHasFixedSize(false)
            }

            executePendingBindings()
        }


        // Add to database
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                with(Gson()) {
                    val sampleUsers = this.fromJson<List<User>>(
                        InputStreamReader(requireContext().assets.open("sample_users.json")),
                        object : TypeToken<List<User>>() {}.type
                    )
                    userDao.insertAll(sampleUsers.toMutableList())
                }
            } catch (e: Exception) {
                debugger(e.localizedMessage)
            }

            withContext(Dispatchers.Main) {
                userViewModel.getUsersStream().collectLatest {
                    usersAdapter.submitData(it)
                }
            }
        }


    }

}
