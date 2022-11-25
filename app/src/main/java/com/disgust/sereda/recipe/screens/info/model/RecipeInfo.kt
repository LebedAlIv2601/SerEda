package com.disgust.sereda.recipe.screens.info.model

import com.disgust.sereda.utils.commonModel.RecipeFavoriteState

data class RecipeInfo(
    val id: Int,
    val name: String,
    val image: String?,
    val time: Int,
    val servings: Int,
    val instructions: List<StepRecipeInstruction>?,
    val diets: List<String>?,
    val ingredients: List<Ingredient>?,
    val calories: Nutrient?,
    val fat: Nutrient?,
    val carbohydrates: Nutrient?,
    val protein: Nutrient?,
    val nutrition: List<Nutrient>?,
    val favoriteState: RecipeFavoriteState = RecipeFavoriteState.NOT_FAVORITE
)

data class StepRecipeInstruction(
    val number: Int,
    val step: String,
    val equipment: List<String>,
)

