package com.disgust.sereda.ingredients.screens.info.interactioin

import com.disgust.sereda.ingredients.screens.info.model.IngredientInfo
import com.disgust.sereda.utils.base.BaseState

sealed class IngredientInfoState : BaseState() {
    object Loading : IngredientInfoState()
    class Success(val data: IngredientInfo) : IngredientInfoState()
    class Error(val error: Exception) : IngredientInfoState()
}