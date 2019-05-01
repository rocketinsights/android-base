package com.rocketinsights.android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.rocketinsights.android.R
import kotlinx.android.synthetic.main.fragment_main.*

class SecondFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
            inflater.inflate(R.layout.fragment_second, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Second Fragment"

        Glide.with(this).load(R.drawable.stock_image).centerCrop().into(stockImage)
    }
}
