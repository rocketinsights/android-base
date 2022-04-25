package com.rocketinsights.android.network.hiltexample

import com.rocketinsights.android.models.recipe_hilt.RecipeDto

data class RecipeSearchResponse(
    val count: Int,
    val results: List<RecipeDto>,
)
