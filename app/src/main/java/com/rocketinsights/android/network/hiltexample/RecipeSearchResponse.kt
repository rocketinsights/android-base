package com.rocketinsights.android.network.hiltexample

import com.rocketinsights.android.models.recipe_hilt.RecipeDto

data class RecipeSearchResponse(
    var count: Int,
    var results: List<RecipeDto>,
)
