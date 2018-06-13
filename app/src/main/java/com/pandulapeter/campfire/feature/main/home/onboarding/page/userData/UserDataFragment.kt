package com.pandulapeter.campfire.feature.main.home.onboarding.page.userData

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.FrameLayout
import com.pandulapeter.campfire.R
import com.pandulapeter.campfire.databinding.FragmentOnboardingUserDataBinding
import com.pandulapeter.campfire.feature.main.options.about.AboutViewModel
import com.pandulapeter.campfire.feature.shared.CampfireFragment
import com.pandulapeter.campfire.util.color
import com.pandulapeter.campfire.util.waitForLayout

class UserDataFragment : CampfireFragment<FragmentOnboardingUserDataBinding, UserDataViewModel>(R.layout.fragment_onboarding_user_data) {

    override val viewModel = UserDataViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val first = getString(R.string.user_data_message_end_part_1)
        val second = getString(R.string.user_data_message_end_part_2)
        val third = getString(R.string.user_data_message_end_part_3)
        val fourth = getString(R.string.user_data_message_end_part_4)
        val fifth = getString(R.string.user_data_message_end_part_5)
        binding.textEnd.movementMethod = LinkMovementMethod.getInstance()
        binding.textEnd.text = SpannableString("$first$second$third$fourth$fifth").apply {
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View?) {
                    CustomTabsIntent.Builder()
                        .setToolbarColor(requireContext().color(R.color.accent))
                        .build()
                        .launchUrl(requireContext(), Uri.parse(AboutViewModel.PRIVACY_POLICY_URL))
                }
            }, first.length, first.length + second.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View?) {
                    CustomTabsIntent.Builder()
                        .setToolbarColor(requireContext().color(R.color.accent))
                        .build()
                        .launchUrl(requireContext(), Uri.parse(AboutViewModel.TERMS_AND_CONDITIONS_URL))
                }
            }, first.length + second.length + third.length, length - fifth.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.linearLayout.apply {
            waitForLayout {
                if (isAdded) {
                    layoutParams = (layoutParams as FrameLayout.LayoutParams).apply { setMargins(0, -getCampfireActivity().toolbarHeight, 0, 0) }
                }
            }
        }
    }
}