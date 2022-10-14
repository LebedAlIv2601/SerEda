package com.disgust.sereda.ingredients.search.di

import com.disgust.sereda.ingredients.search.data.SearchIngredientApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchIngredientModule {

    @Singleton
    @Provides
    fun provideClient(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("x-api-key", "5c53c228b0314373ab33bf60d560add8")
                chain.proceed(builder.build())
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    fun provideSearchIngredientApi(retrofit: Retrofit): SearchIngredientApi {
        return retrofit.create(SearchIngredientApi::class.java)
    }
}