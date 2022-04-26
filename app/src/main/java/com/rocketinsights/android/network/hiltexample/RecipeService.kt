package com.rocketinsights.android.network.hiltexample

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeService {

    @GET("search")
    suspend fun searchRecipe(
        @Query("page") page: Int,
        @Query("query") query: String
    ): RecipeSearchResponse
}
