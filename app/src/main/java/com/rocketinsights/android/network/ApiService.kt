package com.rocketinsights.android.network

import com.rocketinsights.android.network.models.ApiMessage
import retrofit2.http.GET

interface ApiService {
    @GET("31ac04de-4da2-40c6-bf7d-1a37e9ad1ebe")
    suspend fun getMessage(): ApiMessage

    @GET("9b58f48a-048a-4676-a888-ffbeb5e7b762")
    suspend fun getMessages(): List<ApiMessage>
}