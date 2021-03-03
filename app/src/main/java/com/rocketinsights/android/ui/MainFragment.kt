package com.rocketinsights.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.rocketinsights.android.R
import com.rocketinsights.android.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_main, container, false)

    override fun onResume() {
        super.onResume()

        (activity as? AppCompatActivity)?.supportActionBar?.title = "First Fragment"
        mainViewModel.message.observe(this, { data ->
            message.text = data?.text
            stockImage.visibility = View.VISIBLE

            message.setOnClickListener {
                findNavController().navigate(R.id.actionSlideTransition)
            }
        })

        Glide.with(this).load(R.drawable.stock_image).centerCrop().into(stockImage)

        stockImage.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                stockImage to "stockImage"
            )
            findNavController().navigate(R.id.actionGrowTransition, null, null, extras)
        }
    }
}
