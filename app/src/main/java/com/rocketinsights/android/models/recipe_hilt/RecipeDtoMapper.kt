package com.rocketinsights.android.models.recipe_hilt

class RecipeDtoMapper {
    fun mapToDomainModel(model: RecipeDto): Recipe {
        return Recipe(
            id = model.pk,
            title = model.title,
            featuredImage = model.featuredImage,
            rating = model.rating,
            publisher = model.publisher,
            sourceUrl = model.sourceUrl,
            ingredients = model.ingredients,
            dateAdded = model.dateAdded,
            dateUpdated = model.dateUpdated,
        )
    }

    fun toDomainList(initial: List<RecipeDto>): List<Recipe> = initial.map { mapToDomainModel(it) }
}
