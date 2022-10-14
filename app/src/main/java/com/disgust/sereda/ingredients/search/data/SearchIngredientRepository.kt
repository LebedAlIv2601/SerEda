package com.disgust.sereda.ingredients.search.data

import com.disgust.sereda.ingredients.search.model.IngredientItem
import javax.inject.Inject

class SearchIngredientRepository @Inject constructor(
    private val api: SearchIngredientApi
) {

    suspend fun getIngredients(query: String): List<IngredientItem> =
        api.getIngredients(query).results

}