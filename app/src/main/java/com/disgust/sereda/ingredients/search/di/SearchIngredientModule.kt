package com.disgust.sereda.ingredients.search.di

import com.disgust.sereda.ingredients.search.data.SearchIngredientApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class SearchIngredientModule {
    @Provides
    fun provideSearchIngredientApi(retrofit: Retrofit): SearchIngredientApi {
        return retrofit.create(SearchIngredientApi::class.java)
    }
}