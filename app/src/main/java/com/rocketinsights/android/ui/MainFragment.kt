package com.rocketinsights.android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rocketinsights.android.R
import com.rocketinsights.android.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_main, container, false)

    override fun onResume() {
        super.onResume()

        (activity as? AppCompatActivity)?.supportActionBar?.title = "First Fragment"
        mainViewModel.message.observe(this, Observer { data ->
            message.text = data?.text

            message.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_secondFragment)
            }
        })
    }
}
