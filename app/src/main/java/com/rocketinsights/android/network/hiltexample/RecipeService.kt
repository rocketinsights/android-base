package com.rocketinsights.android.network.hiltexample

import com.rocketinsights.android.models.recipe_hilt.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeService {

    @GET("search")
    suspend fun searchRecipe(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("query") query: String
    ): RecipeSearchResponse

    @GET("get")
    suspend fun getRecipe(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): RecipeDto
}
