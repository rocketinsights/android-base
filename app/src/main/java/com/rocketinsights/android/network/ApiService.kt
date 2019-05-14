package com.rocketinsights.android.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rocketinsights.android.models.Message
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("5cd9d1383000004a20c01850")
    fun getMessageAsync(): Deferred<Message>

    companion object {
        fun getApiService(): ApiService = Retrofit.Builder()
                .baseUrl("http://www.mocky.io/v2/")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(ApiService::class.java)
    }
}