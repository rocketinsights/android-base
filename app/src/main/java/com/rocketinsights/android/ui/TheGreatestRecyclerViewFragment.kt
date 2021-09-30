package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.rocketinsights.android.R
import com.rocketinsights.android.databinding.FragmentTheGreatestRecyclerviewBinding
import com.rocketinsights.android.extensions.setupActionBar
import com.rocketinsights.android.extensions.viewBinding
import com.rocketinsights.android.ui.adapters.TheGreatestRecyclerViewAdapter
import com.rocketinsights.android.ui.animators.BaseItemAnimator
import com.rocketinsights.android.ui.animators.FadeInAnimator
import com.rocketinsights.android.ui.animators.FadeInDownAnimator
import com.rocketinsights.android.ui.animators.FadeInLeftAnimator
import com.rocketinsights.android.ui.animators.FadeInRightAnimator
import com.rocketinsights.android.ui.animators.FadeInUpAnimator
import com.rocketinsights.android.ui.animators.FlipInBottomXAnimator
import com.rocketinsights.android.ui.animators.FlipInLeftYAnimator
import com.rocketinsights.android.ui.animators.FlipInRightYAnimator
import com.rocketinsights.android.ui.animators.FlipInTopXAnimator
import com.rocketinsights.android.ui.animators.LandingAnimator
import com.rocketinsights.android.ui.animators.OvershootInLeftAnimator
import com.rocketinsights.android.ui.animators.OvershootInRightAnimator
import com.rocketinsights.android.ui.animators.ScaleInAnimator
import com.rocketinsights.android.ui.animators.ScaleInBottomAnimator
import com.rocketinsights.android.ui.animators.ScaleInLeftAnimator
import com.rocketinsights.android.ui.animators.ScaleInRightAnimator
import com.rocketinsights.android.ui.animators.ScaleInTopAnimator
import com.rocketinsights.android.ui.animators.SlideInDownAnimator
import com.rocketinsights.android.ui.animators.SlideInLeftAnimator
import com.rocketinsights.android.ui.animators.SlideInRightAnimator
import com.rocketinsights.android.ui.animators.SlideInUpAnimator
import com.rocketinsights.android.viewmodels.TheGreatestRecyclerViewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TheGreatestRecyclerViewFragment : Fragment(R.layout.fragment_the_greatest_recyclerview) {
    private val binding by viewBinding(FragmentTheGreatestRecyclerviewBinding::bind)
    private val viewModel by viewModel<TheGreatestRecyclerViewViewModel>()
    private val theGreatestRecyclerViewAdapter = TheGreatestRecyclerViewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar(binding.toolbar)
        setUpBindings()
        setupObservers()
    }

    private fun setUpBindings() {
        binding.theGreatestRecyclerView.adapter = theGreatestRecyclerViewAdapter
        binding.addItem.setOnClickListener { viewModel.addItem() }
        binding.subtractItem.setOnClickListener { viewModel.subtractItem() }
        binding.shuffleItems.setOnClickListener { viewModel.shuffleItems() }
        binding.changeAnimatorItems.setOnClickListener { updateItemAnimator() }
    }

    private fun setupObservers() {
        viewModel.list.observe(viewLifecycleOwner) {
            theGreatestRecyclerViewAdapter.submitList(it)
        }
    }

    private fun updateItemAnimator() {
        // i cannot take credit for these, they're all from here: https://github.com/wasabeef/recyclerview-animators
        val animator: BaseItemAnimator = when ((0..21).random()) {
            1 -> FadeInAnimator()
            2 -> FadeInDownAnimator()
            3 -> FadeInLeftAnimator()
            4 -> FadeInRightAnimator()
            5 -> FadeInUpAnimator()
            6 -> FlipInBottomXAnimator()
            7 -> FlipInLeftYAnimator()
            8 -> FlipInRightYAnimator()
            9 -> FlipInTopXAnimator()
            10 -> LandingAnimator()
            11 -> OvershootInLeftAnimator()
            12 -> OvershootInRightAnimator()
            13 -> ScaleInAnimator()
            14 -> ScaleInBottomAnimator()
            15 -> ScaleInLeftAnimator()
            16 -> ScaleInRightAnimator()
            17 -> ScaleInTopAnimator()
            18 -> SlideInDownAnimator()
            19 -> SlideInLeftAnimator()
            20 -> SlideInRightAnimator()
            else -> SlideInUpAnimator()
        }

        binding.theGreatestRecyclerView.itemAnimator = animator
    }
}