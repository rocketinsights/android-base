package com.rocketinsights.android.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
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
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.removeItem(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.theGreatestRecyclerView)
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

    // i also cannot take credit for this, it's inspired by: https://github.com/kitek/android-rv-swipe-delete
    private abstract class SwipeToDeleteCallback(context: Context) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_trash)
        private val intrinsicWidth = deleteIcon!!.intrinsicWidth
        private val intrinsicHeight = deleteIcon!!.intrinsicHeight
        private val background = ColorDrawable()
        private val backgroundColor = Color.parseColor("#f44336")
        private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) =
            false

        override fun onChildDraw(
            c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
        ) {

            val itemView = viewHolder.itemView
            val itemHeight = itemView.bottom - itemView.top
            val isCanceled = dX == 0f && !isCurrentlyActive

            if (isCanceled) {
                clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                return
            }

            // Draw the red delete background
            background.color = backgroundColor
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            background.draw(c)

            // Calculate position of delete icon
            val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
            val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom = deleteIconTop + intrinsicHeight

            // Draw the delete icon
            deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon?.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
            c?.drawRect(left, top, right, bottom, clearPaint)
        }
    }
}