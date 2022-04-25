package com.rocketinsights.android.network.models.recipe_hilt

import com.google.gson.annotations.SerializedName

data class RecipeDto(
    var pk: Int,
    var title: String,
    var publisher: String,
    @SerializedName("featured_image")
    var featuredImage: String,
    var rating: Int = 0,
    @SerializedName("source_url")
    var sourceUrl: String,
    var ingredients: List<String> = emptyList(),
    @SerializedName("date_added")
    var dateAdded: String,
    @SerializedName("date_updated")
    var dateUpdated: String,
)
