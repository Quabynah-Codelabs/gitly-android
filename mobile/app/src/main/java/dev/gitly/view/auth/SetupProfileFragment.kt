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
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.core.prefs.AuthPrefs
import dev.gitly.databinding.FragmentSetupProfileBinding
import dev.gitly.databinding.PagerExpertiseBinding
import dev.gitly.databinding.PagerPersonalInfoBinding
import dev.gitly.debugger
import dev.gitly.model.data.User
import dev.gitly.view.adapter.CategoryListAdapter
import dev.gitly.viewmodel.CategoryViewModel
import dev.gitly.viewmodel.UserViewModel
import javax.inject.Inject

/**
 * Setup account
 */
@AndroidEntryPoint
class SetupProfileFragment : Fragment() {

    lateinit var binding: FragmentSetupProfileBinding
    private val userViewModel by viewModels<UserViewModel>()
    private val categoryViewModel by viewModels<CategoryViewModel>()

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

            binding.pager.adapter = SetupPagerAdapter(this, user, categoryViewModel, userViewModel)
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

    internal class SetupPagerAdapter(
        private val host: SetupProfileFragment,
        private val user: User?,
        private val categoryViewModel: CategoryViewModel,
        private val userViewModel: UserViewModel
    ) : PagerAdapter() {
        private val context = host.requireContext()
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
                        next.run {

                            setOnClickListener {
                                if (userCountry != null && userDesignation != null && name != null) {
                                    if (user != null) userViewModel.updateUser(
                                        user.copy(
                                            name = name ?: "",
                                            country = userCountry,
                                            designation = userDesignation
                                        )
                                    )
                                    host.binding.pager.currentItem = 1
                                } else
                                    Snackbar.make(
                                        binding.root, "Fill in your details first",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                            }
                        }

                        // set init values
                        name = user?.name
                        userCountry = user?.country ?: context.getString(R.string.default_country)
                        userDesignation = user?.designation

                        // countries
                        val countries = arrayOf(
                            "Ghana",
                            "France",
                            "Brazil",
                            "Nigeria",
                            "England",
                            "US",
                            "South Africa"
                        )
                        userCountry = countries[0]
                        country.setText(userCountry)
                        fabEditCountry.setOnClickListener {
                            MaterialAlertDialogBuilder(context).run {
                                setTitle("Select a country")
                                setSingleChoiceItems(
                                    countries,
                                    countries.indexOf(userCountry)
                                ) { dialog, which ->
                                    userCountry = countries[which]
                                    country.setText(userCountry)
                                    dialog.dismiss()
                                }
                                setPositiveButton("Dismiss") { d, _ -> d.dismiss() }
                                show()
                            }
                        }

                        // designations
                        categoryViewModel.allCategories.observeForever { items ->
                            val designations = items.map { value -> value.name }.toTypedArray()
                            userDesignation = designations[0]
                            designation.setText(userDesignation)
                            fabEditDesignation.setOnClickListener {
                                MaterialAlertDialogBuilder(context).run {
                                    setTitle("Select your designation")
                                    setSingleChoiceItems(
                                        designations,
                                        designations.indexOf(userDesignation)
                                    ) { dialog, which ->
                                        userDesignation = designations[which]
                                        designation.setText(userDesignation)
                                        dialog.dismiss()
                                    }
                                    setPositiveButton("Dismiss") { d, _ -> d.dismiss() }
                                    show()
                                }
                            }
                        }

                        // two-way binding
                        userName.addTextChangedListener {
                            if (!it.isNullOrEmpty()) name = it.toString()
                        }
                        country.addTextChangedListener {
                            if (!it.isNullOrEmpty()) userCountry = it.toString()
                        }

                        designation.addTextChangedListener {
                            if (!it.isNullOrEmpty()) userDesignation = it.toString()
                        }

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
                        done.setOnClickListener {
                            host.findNavController()
                                .navigate(R.id.action_nav_dest_account_setup_to_nav_dest_home)
                        }
                        categoryViewModel.allCategories.observeForever { items ->
                            categoryGrid.run {
                                adapter = CategoryListAdapter(categoryViewModel).apply {
                                    submitList(items)
                                }
                            }
                        }

                        executePendingBindings()
                    }
                    pageTwo
                }
            }
        }

    }
}