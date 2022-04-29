package com.rocketinsights.android.models.recipe_hilt

data class Recipe(
    val id: Int,
    val title: String?,
    val publisher: String,
    val featuredImage: String?,
    val rating: Int = 0,
    val sourceUrl: String,
    val ingredients: List<String>,
    val dateAdded: String,
    val dateUpdated: String
)
