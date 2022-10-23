package com.disgust.sereda.ingredients.screens.info.interactioin

import com.disgust.sereda.ingredients.screens.info.model.IngredientInfo

sealed class IngredientInfoState() {
    object Loading : IngredientInfoState()
    class Success(val data: IngredientInfo) : IngredientInfoState()
    class Error(val error: Exception) : IngredientInfoState()
}