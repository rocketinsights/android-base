package com.rocketinsights.android.repos

import com.rocketinsights.android.models.recipe_hilt.Recipe

interface RecipeRepository {
    suspend fun search(token: String, page: Int, query: String): List<Recipe>?
}
