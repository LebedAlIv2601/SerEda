package com.disgust.sereda.ingredients.search.data

import com.disgust.sereda.ingredients.search.model.IngredientsListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchIngredientApi {

    @GET("food/ingredients/search")
    suspend fun getIngredients(@Query("query") query: String): IngredientsListResponse

}