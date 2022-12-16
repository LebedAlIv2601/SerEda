package com.disgust.sereda.recipe.data

import com.disgust.sereda.recipe.screens.info.model.RecipeInfoResponse
import com.disgust.sereda.recipe.screens.search.model.RecipeListResponse
import com.disgust.sereda.utils.Constants.RECIPES_LIST_PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {

    @GET("recipes/{id}/information?includeNutrition=true")
    suspend fun getRecipeInfo(@Path("id") id: Int): RecipeInfoResponse

    @GET("recipes/complexSearch?number=$RECIPES_LIST_PAGE_SIZE")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("includeIngredients") includeIngredients: String,
        @Query("excludeIngredients") excludeIngredients: String,
        @Query("diet") diet: String,
        @Query("intolerances") intolerances: String,
        @Query("maxReadyTime") maxReadyTime: Int?,
        @Query("minCalories") minCalories: Int?,
        @Query("maxCalories") maxCalories: Int?,
        @Query("offset") offset: Int = 0
    ): RecipeListResponse
}