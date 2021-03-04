package com.rocketinsights.android.network

import com.rocketinsights.android.models.Message
import retrofit2.http.GET

interface ApiService {
    @GET("5cd9d1383000004a20c01850")
    suspend fun getMessage(): Message

    companion object {
        const val BASE_URL = "http://www.mocky.io/v2/"
    }
}