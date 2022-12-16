package com.disgust.sereda.recipe.data

import com.disgust.sereda.recipe.screens.info.model.RecipeInfo
import com.disgust.sereda.recipe.screens.info.model.RecipeInfoResponse
import com.disgust.sereda.recipe.screens.info.model.StepRecipeInstruction
import com.disgust.sereda.recipe.screens.search.model.IngredientFilter
import com.disgust.sereda.recipe.screens.search.model.RecipeItem
import com.disgust.sereda.recipe.screens.search.model.RecipeItemResponse
import com.disgust.sereda.utils.commonModel.RecipeFavoriteState
import com.disgust.sereda.utils.db.favoriteRecipes.FavoriteRecipeDBModel
import com.disgust.sereda.utils.db.filters.FilterRecipeDBModel
import com.disgust.sereda.utils.firebase.model.FavoriteRecipeFirebaseModel

fun RecipeItemResponse.toRecipeItem(): RecipeItem {
    return RecipeItem(
        id = id,
        name = name,
        image = image,
        favoriteState = RecipeFavoriteState.NOT_FAVORITE
    )
}

fun FavoriteRecipeDBModel.toRecipeItem(): RecipeItem {
    return RecipeItem(
        id = id,
        name = name,
        image = image,
        favoriteState = RecipeFavoriteState.FAVORITE
    )
}

fun RecipeItem.toFavoriteRecipeDBModel(): FavoriteRecipeDBModel {
    return FavoriteRecipeDBModel(
        id = id,
        name = name,
        image = image
    )
}

fun RecipeItem.toFavoriteRecipeFirebaseModel(): FavoriteRecipeFirebaseModel {
    return FavoriteRecipeFirebaseModel(
        id = id.toString(),
        name = name,
        image = image
    )
}

fun RecipeInfoResponse.toRecipeInfo(): RecipeInfo {
    val nutrients = nutrition?.nutrients?.toMutableList()
    val calories = nutrients?.find { it.name == "Calories" }
    val fat = nutrients?.find { it.name == "Fat" }
    val carbohydrates = nutrients?.find { it.name == "Carbohydrates" }
    val protein = nutrients?.find { it.name == "Protein" }
    nutrients?.removeAll(listOf(calories, fat, carbohydrates, protein))

    return RecipeInfo(
        id = id,
        name = name,
        image = image,
        time = time,
        servings = servings,
        instructions = instructions?.getOrNull(0)?.step?.map { it ->
            StepRecipeInstruction(
                number = it.number,
                step = it.step,
                equipment = it.equipment.map { it.name }
            )
        },
        diets = diets,
        ingredients = ingredients,
        calories = calories,
        fat = fat,
        carbohydrates = carbohydrates,
        protein = protein,
        nutrition = nutrients,
    )
}

fun RecipeInfo.toFavoriteRecipeFirebaseModel(): FavoriteRecipeFirebaseModel {
    return FavoriteRecipeFirebaseModel(
        id = id.toString(),
        name = name,
        image = image
    )
}

fun RecipeInfo.toFavoriteRecipeDBModel(): FavoriteRecipeDBModel {
    return FavoriteRecipeDBModel(
        id = id,
        name = name,
        image = image
    )
}

fun FavoriteRecipeFirebaseModel.toFavoriteRecipeDBModel(): FavoriteRecipeDBModel {
    return FavoriteRecipeDBModel(
        id = id.toInt(),
        name = name,
        image = image
    )
}

fun FilterRecipeDBModel.toIngredientFilter(): IngredientFilter {
    return IngredientFilter(
        id = id,
        name = name,
        image = image,
        isInclude = isInclude
    )
}

fun IngredientFilter.toFilterRecipeDBModel(): FilterRecipeDBModel {
    return FilterRecipeDBModel(
        id, name, image, isInclude
    )
}