package com.disgust.sereda.ingredients.data

import com.disgust.sereda.ingredients.screens.info.model.IngredientInfoResponse
import com.disgust.sereda.ingredients.screens.search.model.IngredientsListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchIngredientApi {

    @GET("food/ingredients/search")
    suspend fun getIngredients(@Query("query") query: String): IngredientsListResponse

    @GET("food/ingredients/{id}/information?amount=100&unit=g")
    suspend fun getIngredientInfo(@Path("id") id: Int): IngredientInfoResponse

}