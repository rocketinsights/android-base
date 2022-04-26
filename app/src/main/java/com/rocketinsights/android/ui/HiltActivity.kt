package com.rocketinsights.android.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rocketinsights.android.viewmodels.HiltListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HiltActivity : AppCompatActivity() {

    private val viewModel: HiltListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Show data with JetpackCompose in next PR
        viewModel.liveRecipe.observe(this) { recipe ->
            // do nothing for now
        }
    }
}
