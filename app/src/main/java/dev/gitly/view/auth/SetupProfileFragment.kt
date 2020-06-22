package dev.gitly.view.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import dev.gitly.R
import dev.gitly.databinding.FragmentSetupProfileBinding
import dev.gitly.databinding.PagerExpertiseBinding
import dev.gitly.databinding.PagerPersonalInfoBinding

/**
 * Setup account
 */
@AndroidEntryPoint
class SetupProfileFragment : Fragment() {

    private lateinit var binding: FragmentSetupProfileBinding

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

        binding.run {
            pager.run {
                pageMargin = resources.getDimensionPixelSize(R.dimen.spacing_normal)
                offscreenPageLimit = 2
                adapter = SetupPagerAdapter(requireContext())
            }
            indicator.setViewPager(pager)
            executePendingBindings()
        }
    }

}

class SetupPagerAdapter constructor(private val context: Context) : PagerAdapter() {
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