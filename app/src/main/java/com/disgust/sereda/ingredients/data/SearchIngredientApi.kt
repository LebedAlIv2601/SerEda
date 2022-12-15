package com.disgust.sereda.ingredients.data

import com.disgust.sereda.ingredients.screens.info.model.IngredientInfoResponse
import com.disgust.sereda.ingredients.screens.search.model.IngredientsListResponse
import com.disgust.sereda.utils.Constants.INGREDIENTS_LIST_PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchIngredientApi {

    @GET("food/ingredients/search?number=$INGREDIENTS_LIST_PAGE_SIZE")
    suspend fun getIngredients(
        @Query("query") query: String,
        @Query("offset") offset: Int = 0
    ): IngredientsListResponse

    @GET("food/ingredients/{id}/information?amount=100&unit=g")
    suspend fun getIngredientInfo(@Path("id") id: Int): IngredientInfoResponse

}