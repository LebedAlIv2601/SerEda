package com.disgust.sereda.ingredients.data

import com.disgust.sereda.ingredients.screens.info.model.IngredientInfo
import com.disgust.sereda.ingredients.screens.info.model.toIngredientInfo
import com.disgust.sereda.ingredients.screens.search.model.IngredientItem
import com.disgust.sereda.utils.db.SerEdaDatabase
import com.disgust.sereda.utils.db.filters.FilterRecipeDBModel
import javax.inject.Inject

class SearchIngredientRepository @Inject constructor(
    private val api: SearchIngredientApi,
    private val db: SerEdaDatabase
) {

    suspend fun getIngredients(query: String, loadedItems: Int = 0): List<IngredientItem> =
        api.getIngredients(
            query = query,
            offset = loadedItems
        ).results

    suspend fun getIngredientsInfo(id: Int): IngredientInfo =
        api.getIngredientInfo(id).toIngredientInfo()

    fun addFilterRecipe(filter: FilterRecipeDBModel) =
        db.filtersRecipeDao().insertFilterRecipe(filter)

}