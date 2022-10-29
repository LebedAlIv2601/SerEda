package com.disgust.sereda.recipe.di

import com.disgust.sereda.recipe.data.InfoRecipeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class InfoRecipeModule {
    @Provides
    fun provideInfoRecipeApi(retrofit: Retrofit): InfoRecipeApi {
        return retrofit.create(InfoRecipeApi::class.java)
    }
}