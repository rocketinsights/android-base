package com.rocketinsights.android.network.hiltexample

import com.rocketinsights.android.network.models.recipe_hilt.RecipeDto

data class RecipeSearchResponse(
    var count: Int,
    var results: List<RecipeDto>,
)
