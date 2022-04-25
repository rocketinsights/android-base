package com.rocketinsights.android.repos

import com.rocketinsights.android.network.RecipeService
import com.rocketinsights.android.models.recipe_hilt.Recipe
import com.rocketinsights.android.models.recipe_hilt.RecipeDtoMapper

class RecipeRepositoryImplementation(
    private val recipeService: RecipeService,
    private val mapper: RecipeDtoMapper,
) : RecipeRepository {

    override suspend fun search(token: String, page: Int, query: String): List<Recipe> =
        mapper.toDomainList(recipeService.search(token = token, page = page, query = query).results)

    override suspend fun get(token: String, id: Int): Recipe =
        mapper.mapToDomainModel(recipeService.get(token = token, id))
}
