package com.rocketinsights.android.network

import com.rocketinsights.android.network.models.ApiMessage
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("31ac04de-4da2-40c6-bf7d-1a37e9ad1ebe")
    suspend fun getMessage(): ApiMessage

    @GET("9b58f48a-048a-4676-a888-ffbeb5e7b762")
    suspend fun getMessages(): List<ApiMessage>

    @FormUrlEncoded
    @POST("8a532566-f1be-476c-8078-bc44ca42cbd9")
    suspend fun refreshToken(@Field("token") token: String)
}