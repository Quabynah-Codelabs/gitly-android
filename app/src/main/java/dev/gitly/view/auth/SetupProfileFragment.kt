package dev.gitly.view.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.databinding.FragmentSetupProfileBinding
import dev.gitly.databinding.PagerExpertiseBinding
import dev.gitly.databinding.PagerPersonalInfoBinding
import dev.gitly.debugger
import dev.gitly.model.data.User
import dev.gitly.viewmodel.AuthViewModel
import dev.gitly.viewmodel.UserViewModel
import javax.inject.Inject

/**
 * Setup account
 */
@AndroidEntryPoint
class SetupProfileFragment : Fragment() {

    private lateinit var binding: FragmentSetupProfileBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    @Inject
    lateinit var authPrefs: AuthPrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_setup_profile, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // observe current user
        userViewModel.currentUser.observe(viewLifecycleOwner, { user ->
            debugger("Observing profile for ${user?.name}")

            binding.pager.adapter = SetupPagerAdapter(requireContext(), user)
            binding.indicator.setViewPager(binding.pager)
        })

        // apply binding
        binding.run {
            pager.run {
                pageMargin = resources.getDimensionPixelSize(R.dimen.spacing_normal)
                offscreenPageLimit = 2
            }
            executePendingBindings()
        }
    }

    class SetupPagerAdapter(
        private val context: Context,
        private val user: User?
    ) : PagerAdapter() {
        private val inflater by lazy { LayoutInflater.from(context) }

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun getCount(): Int = 2

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any =
            getPage(container, position).also {
                container.addView(it)
            }

        private fun getPage(container: ViewGroup, position: Int): View {
            return when (position) {
                0 -> {
                    val pageOne = inflater.inflate(
                        R.layout.pager_personal_info,
                        container,
                        false
                    )
                    val binding = DataBindingUtil.bind<PagerPersonalInfoBinding>(pageOne)!!

                    // bind here
                    binding.run {
                        currentUser = user
                        executePendingBindings()
                    }
                    pageOne
                }

                else -> {
                    val pageTwo = inflater.inflate(
                        R.layout.pager_expertise,
                        container,
                        false
                    )
                    val binding = DataBindingUtil.bind<PagerExpertiseBinding>(pageTwo)!!

                    // bind here
                    binding.run {

                        executePendingBindings()
                    }
                    pageTwo
                }
            }
        }


    }
}