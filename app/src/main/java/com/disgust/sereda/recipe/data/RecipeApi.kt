package com.disgust.sereda.recipe.data

import com.disgust.sereda.recipe.screens.info.model.RecipeInfoResponse
import com.disgust.sereda.recipe.screens.search.model.RecipeListResponse
import com.disgust.sereda.recipe.screens.search.model.RecipeRandomListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {

    @GET("recipes/{id}/information?includeNutrition=true")
    suspend fun getRecipeInfo(@Path("id") id: Int): RecipeInfoResponse

    @GET("recipes/random?number=10")
    suspend fun getRandomRecipes(): RecipeRandomListResponse

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(@Query("query") query: String): RecipeListResponse
}