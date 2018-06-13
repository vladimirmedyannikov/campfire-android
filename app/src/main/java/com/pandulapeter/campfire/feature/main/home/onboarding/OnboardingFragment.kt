package com.pandulapeter.campfire.feature.main.home.onboarding

import android.os.Bundle
import android.transition.Fade
import android.view.View
import android.widget.FrameLayout
import com.pandulapeter.campfire.R
import com.pandulapeter.campfire.databinding.FragmentOnboardingBinding
import com.pandulapeter.campfire.feature.main.home.HomeContainerFragment
import com.pandulapeter.campfire.feature.shared.CampfireFragment
import com.pandulapeter.campfire.util.addPageScrollListener
import com.pandulapeter.campfire.util.waitForLayout

class OnboardingFragment : CampfireFragment<FragmentOnboardingBinding, OnboardingViewModel>(R.layout.fragment_onboarding) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Fade()
    }

    override val viewModel = OnboardingViewModel(::navigateToHome, {
        if (binding.viewPager.currentItem + 1 < binding.viewPager.adapter?.count ?: 0) {
            binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
        } else {
            navigateToHome()
        }
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.addPageScrollListener(
            onPageSelected = { viewModel.isOnLastPage.set(it + 1 == binding.viewPager.adapter?.count ?: 0) },
            onPageScrolled = { index, offset ->
                if (index == binding.viewPager.adapter?.count) {

                }
            }
        )
        binding.viewPager.adapter = OnboardingAdapter(childFragmentManager)
        binding.root.run {
            waitForLayout {
                if (isAdded) {
                    layoutParams = (layoutParams as FrameLayout.LayoutParams).apply { setMargins(0, -getCampfireActivity().toolbarHeight, 0, 0) }
                }
            }
        }
    }

    private fun navigateToHome() {
        (parentFragment as? HomeContainerFragment)?.navigateToHome()
    }
}