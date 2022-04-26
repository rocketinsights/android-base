package com.rocketinsights.android.repos

import com.rocketinsights.android.models.recipe_hilt.Recipe
import com.rocketinsights.android.models.recipe_hilt.RecipeDtoMapper
import com.rocketinsights.android.network.hiltexample.RecipeService

class RecipeRepositoryImplementation(
    private val recipeService: RecipeService,
    private val mapper: RecipeDtoMapper,
) : RecipeRepository {

    override suspend fun search(page: Int, query: String): List<Recipe> =
        mapper.toDomainList(recipeService.searchRecipe(page = page, query = query).results)
}
