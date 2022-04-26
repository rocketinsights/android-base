package com.rocketinsights.android.di.hilt

import com.google.gson.GsonBuilder
import com.rocketinsights.android.models.recipe_hilt.RecipeDtoMapper
import com.rocketinsights.android.network.hiltexample.RecipeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object HiltNetworkModule {

    private var okHttpClient: OkHttpClient? = null

    @Singleton
    @Provides
    fun providesRecipeMapper(): RecipeDtoMapper = RecipeDtoMapper()

    @Singleton
    @Provides
    fun provideRecipeService(): RecipeService {
        return Retrofit.Builder()
            .baseUrl("https://food2fork.ca/api/recipe/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(buildClientHttp())
            .build()
            .create(RecipeService::class.java)
    }


    private fun buildClientHttp(): OkHttpClient? {
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(buildInterceptor())
            .build()

        return okHttpClient
    }

    private fun buildInterceptor(): Interceptor {
        var interceptor = Interceptor { chain ->
            var request = chain.request()

            request = request.newBuilder()
                .addHeader(
                    "Authorization",
                    "Token 9c8b06d329136da358c2d00e76946b0111ce2c48"
                )
                .build()

            chain.proceed(request = request)
        }
        return interceptor
    }
}

