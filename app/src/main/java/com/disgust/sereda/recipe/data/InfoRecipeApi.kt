package com.disgust.sereda.recipe.data

import com.disgust.sereda.recipe.screens.info.model.RecipeInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface InfoRecipeApi {

    @GET("recipes/{id}/information?includeNutrition=false")
    suspend fun getRecipeInfo(@Path("id") id: Int): RecipeInfoResponse

}