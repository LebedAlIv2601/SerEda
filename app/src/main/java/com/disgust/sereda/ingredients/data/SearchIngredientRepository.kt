package com.disgust.sereda.ingredients.data

import com.disgust.sereda.ingredients.screens.info.model.IngredientInfo
import com.disgust.sereda.ingredients.screens.info.model.toIngredientInfo
import com.disgust.sereda.ingredients.screens.search.model.IngredientItem
import javax.inject.Inject

class SearchIngredientRepository @Inject constructor(
    private val api: SearchIngredientApi
) {

    suspend fun getIngredients(query: String): List<IngredientItem> =
        api.getIngredients(query).results

    suspend fun getIngredientsInfo(id: Int): IngredientInfo =
        api.getIngredientInfo(id).toIngredientInfo()
}